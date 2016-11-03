package main.be.kdg.bagageafhandeling.transport.exceptions;

import java.io.FileNotFoundException;

/**
 * Created by Michiel on 3/11/2016.
 */
public class RecordReaderException extends Throwable {
    public RecordReaderException(String s, Exception e) {
        super(s,e);
    }
}
