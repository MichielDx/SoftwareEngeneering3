package main.be.kdg.bagageafhandeling.transport.engines;

import main.be.kdg.bagageafhandeling.transport.models.bagage.Bagage;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BagageRepository;
import main.be.kdg.bagageafhandeling.transport.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Connector;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Segment;
import main.be.kdg.bagageafhandeling.transport.models.dto.BagageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.models.enums.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.models.SensorMessage;
import main.be.kdg.bagageafhandeling.transport.services.route.ConveyorRepository;
import main.be.kdg.bagageafhandeling.transport.services.route.RouteInput;
import main.be.kdg.bagageafhandeling.transport.services.route.RouteOutput;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Michiel on 4/11/2016.
 */
public class RouteScheduler implements Observer {

    private RouteOutput routeOutput;
    private RouteInput routeInput;
    private DelayMethod delayMethod;
    private BagageMessageDTO result;
    private long delay;
    private Map<Integer,Integer> securityList;
    private Logger logger = Logger.getLogger(RouteScheduler.class);
    private ConveyorRepository conveyorRepository;

    public RouteScheduler(DelayMethod delayMethod, long delay, Map<Integer,Integer> securityList) {
        this.delayMethod = delayMethod;
        this.securityList = securityList;
        this.delay = delay;
        initialize();
    }

    private void initialize() {
        this.routeInput = new RouteInput();
        conveyorRepository = new ConveyorRepository();
        routeInput.initializeAPI(new ConveyorServiceAPI());
        try {
            routeInput.initializeRabbitMQ(this);
        } catch (MessageInputException e) {
            logger.error(e.getMessage());
        }
        this.routeOutput = new RouteOutput();
    }

    private void doTask(BagageMessageDTO bagageMessageDTO) {
        if(securityList.containsKey(bagageMessageDTO.getBagageID()) && securityList.containsValue(bagageMessageDTO.getConveyorID())){
            return;
        }
        Bagage bagage = BagageRepository.getBagage(bagageMessageDTO.getBagageID());
        long timedifference = System.currentTimeMillis() - bagage.getTimestamp().getTime();
        Conveyor originConveyor = null;
        Conveyor destinationConveyor = null;
        Conveyor currentConveyor = null;
        try {
            if(conveyorRepository.contains(bagageMessageDTO.getConveyorID())){
                destinationConveyor = conveyorRepository.getConveyor(bagageMessageDTO.getConveyorID());
            }else {
                destinationConveyor = routeInput.getConveyor(bagageMessageDTO.getConveyorID());
                logger.info("Succesfully received conveyor with ID " + destinationConveyor.getConveyorID() + " from proxy");
            }

            if(conveyorRepository.contains(bagage.getSensorID())){
                originConveyor = conveyorRepository.getConveyor(bagage.getSensorID());
            }else {
                originConveyor = routeInput.getConveyor(bagage.getSensorID());
                logger.info("Succesfully received conveyor with ID " + originConveyor.getConveyorID() + " from proxy");
            }

            if(conveyorRepository.contains(bagage.getConveyorID())){
                currentConveyor = conveyorRepository.getConveyor(bagage.getConveyorID());
            }else {
                currentConveyor = routeInput.getConveyor(bagage.getConveyorID());
                logger.info("Succesfully received conveyor with ID " + currentConveyor.getConveyorID() + " from proxy");
            }
        } catch (APIException e) {
            destinationConveyor = null;
            currentConveyor = null;
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
        if (destinationConveyor == null || currentConveyor == null) delayMethod = DelayMethod.FIXED;
        if (delayMethod == DelayMethod.FIXED) {
            publish(bagageMessageDTO, delay);
        } else {
            publish(bagageMessageDTO, calculateDelay(destinationConveyor, currentConveyor, originConveyor, timedifference));
        }
    }



    private long calculateDelay(Conveyor destinationConveyor, Conveyor currentConveyor, Conveyor originConveyor, long timedifference) {
        long delayInMilliSeconds = 0;
        long conveyorCycleDuration = (currentConveyor.getLength() / currentConveyor.getSpeed()) * 1000;
        long currentCycle = timedifference % conveyorCycleDuration;
        long timeToOut = 0;
        long durationToOutPoint = 0;
        for (Segment s : currentConveyor.getSegments()) {
            if (s.getOutPoint() == destinationConveyor.getConveyorID() && s.getInPoint() == originConveyor.getConveyorID()) {
                durationToOutPoint = (s.getDistance() / currentConveyor.getSpeed()) * 1000;
                timeToOut = (durationToOutPoint) * 1000;
            }
        }
        if ((timeToOut - currentCycle) > 0) {
            delayInMilliSeconds += timeToOut - currentCycle;
        } else {
            delayInMilliSeconds += (conveyorCycleDuration - currentCycle) + durationToOutPoint;
        }
        for(Connector connector : currentConveyor.getConnectors()){
            if(connector.getType().equals("outgoing") && connector.getConnectedConveyorID() == destinationConveyor.getConveyorID()){
                delayInMilliSeconds += (connector.getLength()/connector.getSpeed());
            }
        }
        return delayInMilliSeconds;
    }

    private void publish(BagageMessageDTO bagageMessageDTO, long sleep) {
        new Thread(() -> {
            try {
                Thread.sleep(sleep);
                routeOutput.publish(new SensorMessage(bagageMessageDTO.getBagageID(), bagageMessageDTO.getConveyorID(), new Date()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void update(Observable o, Object arg) {
        result = (BagageMessageDTO) arg;
        logger.info("Retrieved BagageMessageDTO from rabbitMQ: " + result.toString());
        new Thread(() -> doTask(result)).start();
    }
}




