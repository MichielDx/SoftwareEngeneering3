package main.be.kdg.bagageafhandeling.transport.engine;


import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.model.DTO.BagageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.services.Route.RouteInput;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class RouteScheduler implements Runnable, Observer {
    private BagageMessageDTO result;
    private RouteInput routeInput;
    private Logger logger = Logger.getLogger(RouteScheduler.class);

    public RouteScheduler(){
        routeInput = new RouteInput();
    }

    @Override
    public void run() {
        try {
            routeInput.initializeRabbitMQ(this);
        } catch (MessageInputException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        result = (BagageMessageDTO) arg;
        SensorScheduler.addBagageMessageDTO(result);
        logger.info("Retrieved BagageMessageDTO from rabbitMQ: " + result.toString());
    }
}
