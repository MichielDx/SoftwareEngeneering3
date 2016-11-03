package main.be.kdg.bagageafhandeling.transport.engine;


import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.services.RouteInput;
import org.apache.log4j.Logger;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class RouteScheduler implements Runnable {
    RouteInput routeInput;
    Logger logger = Logger.getLogger(RouteScheduler.class);

    public RouteScheduler(){
        routeInput = new RouteInput();
    }

    @Override
    public void run() {
        try {
            routeInput.initialize();
        } catch (MessageInputException e) {
            logger.error(e.getMessage());

        }
    }
}
