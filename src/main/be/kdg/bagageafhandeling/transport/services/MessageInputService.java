package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.model.BagageMessageDTO;

/**
 * Created by Michiel on 3/11/2016.
 */
public interface MessageInputService {
    /**
     * Start up by supplying a callback object
     */
    BagageMessageDTO initialize() throws MessageInputException;


    /**
     * Close all connections to this service
     */
    void shutdown() throws MessageInputException;
}
