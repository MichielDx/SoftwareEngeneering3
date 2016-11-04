package main.be.kdg.bagageafhandeling.transport.engine;

import main.be.kdg.bagageafhandeling.transport.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.model.Conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.model.DTO.BagageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.model.Enum.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.services.Route.RouteInput;
import main.be.kdg.bagageafhandeling.transport.services.Route.RouteOutput;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Michiel on 4/11/2016.
 */
public class RouteScheduler implements Runnable, Observer {
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

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                doTask();
            } catch (APIException e) {
                logger.error(e.getMessage());
                logger.error(e.getCause().getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doTask() throws APIException {
        if (bagageMessageDTOs.size() != 0) {
            for (BagageMessageDTO bagageMessageDTO : bagageMessageDTOs) {

                conveyor = routeInput.getConveyor(bagageMessageDTO.getConveyorID());
                logger.info("Conveyor received " + conveyor.getConveyorID());
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        result = (BagageMessageDTO) arg;
        bagageMessageDTOs.add(result);
        logger.info("Retrieved BagageMessageDTO from rabbitMQ: " + result.toString());
    }
}




