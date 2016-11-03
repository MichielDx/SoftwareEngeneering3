package main.be.kdg.bagageafhandeling.transport.exceptions;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class RecordWriterException extends Exception{
    public RecordWriterException(String message, Exception e) {
        super(message,e);
    }
}
