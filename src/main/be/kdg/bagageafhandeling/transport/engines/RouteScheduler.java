package main.be.kdg.bagageafhandeling.transport.engines;

import main.be.kdg.bagageafhandeling.transport.exceptions.RepositoryException;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.repository.BaggageRepository;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Connector;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Segment;
import main.be.kdg.bagageafhandeling.transport.models.enums.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.models.SensorMessage;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.transport.repository.ConveyorRepository;
import main.be.kdg.bagageafhandeling.transport.services.Publisher;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * a RouteScheduler Observes a message broker.
 * When updated a {@link BaggageMessageDTO} object is passed as argument.
 * A concurrent-safe thread is started to switch the baggage from his current conveyor to the next conveyor in the route
 * After a set or calculated amount of time a {@link SensorMessage} is published containing the current conveyor the baggage is on
 */
public class RouteScheduler implements Observer {

    private Publisher publisher;
    private ConveyorService conveyorAPI;
    private DelayMethod delayMethod;
    private BaggageMessageDTO result;
    private long delay;
    private Map<Integer, Integer> securityList;
    private Logger logger = Logger.getLogger(RouteScheduler.class);
    private ConveyorRepository conveyorRepository;
    private BaggageRepository baggageRepository;

    public RouteScheduler(BaggageRepository baggageRepository, ConveyorRepository conveyorRepository, DelayMethod delayMethod, long delay, Map<Integer, Integer> securityList, ConveyorService conveyorService, Publisher routePublisher) {
        this.baggageRepository = baggageRepository;
        this.delayMethod = delayMethod;
        this.securityList = securityList;
        this.delay = delay;
        this.conveyorAPI = conveyorService;
        this.conveyorRepository = conveyorRepository;
        this.publisher = routePublisher;
    }


    private void doTask(BaggageMessageDTO baggageMessageDTO) {
        if (securityList.containsKey(baggageMessageDTO.getBaggageID()) && securityList.containsValue(baggageMessageDTO.getConveyorID())) {
            return;
        }
        Baggage baggage = null;
        try {
            baggage = baggageRepository.getBaggage(baggageMessageDTO.getBaggageID());
            logger.info("Received RouteMessage for baggage ID = " + baggageMessageDTO.getBaggageID() + " attempting to switch baggage: to conveyor ID = " + baggageMessageDTO.getConveyorID());
            long timedifference = System.currentTimeMillis() - baggage.getTimestamp().getTime();
            Conveyor originConveyor = getConveyor(baggage.getSensorID());
            Conveyor destinationConveyor = getConveyor(baggageMessageDTO.getConveyorID());
            Conveyor currentConveyor = getConveyor(baggage.getConveyorID());

            if (destinationConveyor == null || currentConveyor == null) delayMethod = DelayMethod.FIXED;
            if (delayMethod == DelayMethod.FIXED) {
                publish(baggage, delay);
            } else {
                baggage.setConveyorID(destinationConveyor.getConveyorID());
                baggage.setSensorID(currentConveyor.getConveyorID());
                baggageRepository.updateBagage(baggage);
                publish(baggage, calculateDelay(destinationConveyor, currentConveyor, originConveyor, timedifference));
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage());
        }


    }

    private Conveyor getConveyor(int conveyorID) {
        Conveyor conveyor = null;
        try {
            if (conveyorRepository.contains(conveyorID)) {
                conveyor = conveyorRepository.getConveyor(conveyorID);
            } else {
                conveyor = conveyorAPI.fetchConveyor(conveyorID);
                conveyorRepository.addConveyor(conveyor);
            }
        } catch (APIException e) {
            conveyor = null;
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
        return conveyor;
    }

    /**
     * @param timedifference how long the baggage has been on the current conveyor
     * @return a calculated delay based on the length and speed of the conveyors and connectors
     */
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
        for (Connector connector : currentConveyor.getConnectors()) {
            if (connector.getType().equals("outgoing") && connector.getConnectedConveyorID() == destinationConveyor.getConveyorID()) {
                delayInMilliSeconds += ((connector.getLength() / connector.getSpeed()) * 1000);
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




