package main.be.kdg.bagageafhandeling.transport.engine;

import main.be.kdg.bagageafhandeling.transport.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.model.Conveyor.Connector;
import main.be.kdg.bagageafhandeling.transport.model.Conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.model.Conveyor.Segment;
import main.be.kdg.bagageafhandeling.transport.model.DTO.BagageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.model.Enum.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.model.SensorMessage;
import main.be.kdg.bagageafhandeling.transport.services.Route.RouteInput;
import main.be.kdg.bagageafhandeling.transport.services.Route.RouteOutput;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Michiel on 4/11/2016.
 */
public class RouteScheduler implements Observer {
    private List<BagageMessageDTO> bagageMessageDTOs = new LinkedList<>();
    private RouteOutput routeOutput;
    private RouteInput routeInput;
    private DelayMethod delayMethod;
    private BagageMessageDTO result;
    private Conveyor conveyor;
    private long delay;
    private Logger logger = Logger.getLogger(RouteScheduler.class);

    public RouteScheduler(DelayMethod delayMethod, long delay) {
        this.delayMethod = delayMethod;
        this.delay = delay;
        initialize();
    }

    private void initialize() {
        this.routeInput = new RouteInput();
        routeInput.initializeAPI(new ConveyorServiceAPI());
        try {
            routeInput.initializeRabbitMQ(this);
        } catch (MessageInputException e) {
            logger.error(e.getMessage());
        }
        this.routeOutput = new RouteOutput();
    }

    private void doTask() {
        for (BagageMessageDTO bagageMessageDTO : bagageMessageDTOs) {
            try {
                conveyor = routeInput.getConveyor(bagageMessageDTO.getConveyorID());
                logger.info("Succesfully received conveyor with ID " + conveyor.getConveyorID() + " from proxy");

            } catch (APIException e) {
                conveyor = null;
                logger.error(e.getMessage());
                logger.error(e.getCause().getMessage());
            }
            if (conveyor == null) delayMethod = DelayMethod.FIXED;
            if (delayMethod == DelayMethod.FIXED) {
                try {
                    Thread.sleep(delay);
                    routeOutput.publish(new SensorMessage(bagageMessageDTO.getBagageID(), bagageMessageDTO.getConveyorID(), new Date()));
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            } else {
                calculateDelay(conveyor);
            }
        }
    }

    private int calculateDelay(Conveyor conveyor) {
        int delayInSeconds = 0;
        Conveyor currentConveyor;
        for (Segment s : conveyor.getSegments()) {
            if (s.getOutPoint() == conveyor.getConveyorID()) {
                delayInSeconds += s.getDistance() / conveyor.getSpeed();
            }
        }
        for (Connector c : conveyor.getConnectors()) {
            if (c.getConnectedConveyorID() == conveyor.getConveyorID()) {
                delayInSeconds += c.getLength() / c.getSpeed();
            }
        }
        return delayInSeconds;
    }


    @Override
    public void update(Observable o, Object arg) {
        result = (BagageMessageDTO) arg;
        bagageMessageDTOs.add(result);
        doTask();
        logger.info("Retrieved BagageMessageDTO from rabbitMQ: " + result.toString());
    }
}




