package main.be.kdg.bagageafhandeling.transport.exceptions;

/**
 * Created by Michiel on 2/11/2016.
 */
public class MessageOutputException extends Exception{
    public MessageOutputException(String message, Exception e) {
        super(message,e);
    }
}
