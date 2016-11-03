package main.be.kdg.bagageafhandeling.transport.exceptions;

/**
 * Created by Michiel on 3/11/2016.
 */
public class MessageInputException extends Exception {
    public MessageInputException(String message, Exception e) {
        super(message, e);
    }
}
