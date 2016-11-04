package main.be.kdg.bagageafhandeling.transport.model;

import main.be.kdg.bagageafhandeling.transport.adapters.out.RabbitMQ;

import java.util.Date;

/**
 * Created by Michiel on 4/11/2016.
 */
public class SensorMessage {
    private int bagageID;
    private int conveyorID;
    private Date timestamp;

    public SensorMessage(int bagageID, int conveyorID, Date timestamp) {
        this.bagageID = bagageID;
        this.conveyorID = conveyorID;
        this.timestamp = timestamp;
    }
}
