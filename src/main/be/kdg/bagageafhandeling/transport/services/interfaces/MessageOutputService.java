package main.be.kdg.bagageafhandeling.transport.services.interfaces;

/**
 * Created by Michiel on 2/11/2016.
 */

import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;

/**
 * An async service that can be used to receive messages from a communication interface (e.g. message queue)
 */
public interface MessageOutputService {
    /**
     * Start up by supplying a callback object
     */
    void initialize() throws MessageOutputException;


    /**
     * Close all connections to this service
     */
    void shutdown() throws MessageOutputException;
}