package main.be.kdg.bagageafhandeling.transport.engines;

import main.be.kdg.bagageafhandeling.transport.exceptions.RepositoryException;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageRepository;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Connector;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Segment;
import main.be.kdg.bagageafhandeling.transport.models.enums.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.models.SensorMessage;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.transport.services.route.ConveyorRepository;
import main.be.kdg.bagageafhandeling.transport.services.Publisher;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Michiel on 4/11/2016.
 */
public class RouteScheduler implements Observer {

    private Publisher publisher;
    private ConveyorService conveyorAPI;
    private DelayMethod delayMethod;
    private BaggageMessageDTO result;
    private long delay;
    private Map<Integer,Integer> securityList;
    private Logger logger = Logger.getLogger(RouteScheduler.class);
    private ConveyorRepository conveyorRepository;

    public RouteScheduler(DelayMethod delayMethod, long delay, Map<Integer,Integer> securityList,ConveyorService conveyorService, Publisher routePublisher) {
        this.delayMethod = delayMethod;
        this.securityList = securityList;
        this.delay = delay;
        this.conveyorAPI = conveyorService;
        this.conveyorRepository = new ConveyorRepository();
        this.publisher = routePublisher;
    }


    private void doTask(BaggageMessageDTO baggageMessageDTO) {
        if(securityList.containsKey(baggageMessageDTO.getBaggageID()) && securityList.containsValue(baggageMessageDTO.getConveyorID())){
            return;
        }

        Baggage baggage = null;
        try {
            baggage = BaggageRepository.getBaggage(baggageMessageDTO.getBaggageID());
        } catch (RepositoryException e) {
            e.getMessage();
        }

        long timedifference = System.currentTimeMillis() - baggage.getTimestamp().getTime();
        Conveyor originConveyor = null;
        Conveyor destinationConveyor = null;
        Conveyor currentConveyor = null;
        logger.info("Received RouteMessage for baggage ID = "  + baggage.getBaggageID() + " attempting to switch baggage: to conveyor ID = " +baggageMessageDTO.getConveyorID());
        try {
            if(conveyorRepository.contains(baggageMessageDTO.getConveyorID())){
                destinationConveyor = conveyorRepository.getConveyor(baggageMessageDTO.getConveyorID());
            }else {
                destinationConveyor = conveyorAPI.fetchConveyor(baggageMessageDTO.getConveyorID());
            }

            if(conveyorRepository.contains(baggage.getSensorID())){
                originConveyor = conveyorRepository.getConveyor(baggage.getSensorID());
            }else {
                originConveyor = conveyorAPI.fetchConveyor(baggage.getSensorID());
            }

            if(conveyorRepository.contains(baggage.getConveyorID())){
                currentConveyor = conveyorRepository.getConveyor(baggage.getConveyorID());
            }else {
                currentConveyor = conveyorAPI.fetchConveyor(baggage.getConveyorID());
            }
        } catch (APIException e) {
            destinationConveyor = null;
            currentConveyor = null;
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
        if (destinationConveyor == null || currentConveyor == null) delayMethod = DelayMethod.FIXED;
        if (delayMethod == DelayMethod.FIXED) {
            publish(baggage, delay);
        } else {
            baggage.setConveyorID(destinationConveyor.getConveyorID());
            baggage.setSensorID(currentConveyor.getConveyorID());
            BaggageRepository.updateBagage(baggage);
            publish(baggage, calculateDelay(destinationConveyor, currentConveyor, originConveyor, timedifference));
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
                timeToOut = durationToOutPoint;
            }
        }
        if ((timeToOut - currentCycle) > 0) {
            delayInMilliSeconds += timeToOut - currentCycle;
        } else {
            delayInMilliSeconds += (conveyorCycleDuration - currentCycle) + durationToOutPoint;
        }
        for(Connector connector : currentConveyor.getConnectors()){
            if(connector.getType().equals("outgoing") && connector.getConnectedConveyorID() == destinationConveyor.getConveyorID()){
                delayInMilliSeconds += ((connector.getLength()/connector.getSpeed())*1000);
            }
        }
        return delayInMilliSeconds;
    }

    private void publish(Baggage baggage, long sleep) {
        new Thread(() -> {
            try {
                Thread.sleep(sleep);
                publisher.publish(new SensorMessage(baggage.getBaggageID(), baggage.getConveyorID(), new Date()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void update(Observable o, Object arg) {
        result = (BaggageMessageDTO) arg;
        new Thread(() -> doTask(result)).start();
    }
}




