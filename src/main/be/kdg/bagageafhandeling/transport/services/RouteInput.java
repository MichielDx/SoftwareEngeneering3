package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.adapters.in.RabbitMQ;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.model.DTO.BagageMessageDTO;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class RouteInput implements Observer {
    private BagageMessageDTO result;
    private Logger logger = Logger.getLogger(RouteInput.class);
    private MessageInputService rabbitMQ;

    public RouteInput() {
        rabbitMQ = new RabbitMQ("organiserRouteQueue");
    }

    public void initialize() throws MessageInputException {
        rabbitMQ.initialize();
        rabbitMQ.addObserver(this);
        rabbitMQ.retrieve();

    }

    @Override
    public void update(Observable o, Object arg) {
        result = (BagageMessageDTO) arg;
        logger.info("Retrieved BagageMessageDTO from rabbitMQ: " + result.toString());
    }
}
