package main.be.kdg.bagageafhandeling.transport.engines;

import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageRepository;
import main.be.kdg.bagageafhandeling.transport.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Connector;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Segment;
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
    private BaggageMessageDTO result;
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
        /*try {
            routeInput.initializeRabbitMQ(this);
        } catch (MessageInputException e) {
            logger.error(e.getMessage());
        }*/
        this.routeOutput = new RouteOutput();
    }

    private void doTask(BaggageMessageDTO baggageMessageDTO) {
        if(securityList.containsKey(baggageMessageDTO.getBaggageID()) && securityList.containsValue(baggageMessageDTO.getConveyorID())){
            return;
        }
        Baggage baggage = BaggageRepository.getBagage(baggageMessageDTO.getBaggageID());
        long timedifference = System.currentTimeMillis() - baggage.getTimestamp().getTime();
        Conveyor originConveyor = null;
        Conveyor destinationConveyor = null;
        Conveyor currentConveyor = null;
        try {
            if(conveyorRepository.contains(baggageMessageDTO.getConveyorID())){
                destinationConveyor = conveyorRepository.getConveyor(baggageMessageDTO.getConveyorID());
            }else {
                destinationConveyor = routeInput.getConveyor(baggageMessageDTO.getConveyorID());
                logger.info("Succesfully received conveyor with ID " + destinationConveyor.getConveyorID() + " from proxy");
            }

            if(conveyorRepository.contains(baggage.getSensorID())){
                originConveyor = conveyorRepository.getConveyor(baggage.getSensorID());
            }else {
                originConveyor = routeInput.getConveyor(baggage.getSensorID());
                logger.info("Succesfully received conveyor with ID " + originConveyor.getConveyorID() + " from proxy");
            }

            if(conveyorRepository.contains(baggage.getConveyorID())){
                currentConveyor = conveyorRepository.getConveyor(baggage.getConveyorID());
            }else {
                currentConveyor = routeInput.getConveyor(baggage.getConveyorID());
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
            publish(baggageMessageDTO, delay);
        } else {
            publish(baggageMessageDTO, calculateDelay(destinationConveyor, currentConveyor, originConveyor, timedifference));
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

    private void publish(BaggageMessageDTO baggageMessageDTO, long sleep) {
        new Thread(() -> {
            try {
                Thread.sleep(sleep);
                routeOutput.publish(new SensorMessage(baggageMessageDTO.getBaggageID(), baggageMessageDTO.getConveyorID(), new Date()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void update(Observable o, Object arg) {
        result = (BaggageMessageDTO) arg;
        logger.info("Retrieved BaggageMessageDTO from rabbitMQ: " + result.toString());
        new Thread(() -> doTask(result)).start();
    }
}




