package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageOutputService;
import org.apache.log4j.Logger;


/**
 * Created by Michiel on 4/11/2016.
 */
public class Publisher {
    private MessageOutputService messageOutputService;
    private PublisherXmlServiceImpl xmlService;
    private Logger logger = Logger.getLogger(Publisher.class);

    public Publisher(MessageOutputService messageOutputService) {
        this.messageOutputService = messageOutputService;
        initialize();
    }

    private void initialize(){
        try {
            this.messageOutputService.initialize();
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public synchronized void publish(Object object) {
        try {
            messageOutputService.publish(xmlService.serialize(object));
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }
}
