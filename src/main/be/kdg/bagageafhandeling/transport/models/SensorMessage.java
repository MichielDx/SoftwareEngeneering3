package main.be.kdg.bagageafhandeling.transport.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Michiel on 4/11/2016.
 */
@XmlRootElement(name="sensor")
public class SensorMessage {
    @XmlElement
    private int baggageID;
    @XmlElement
    private int conveyorID;
    @XmlElement
    private Date timestamp;

    public SensorMessage() {
    }

    public SensorMessage(int baggageID, int conveyorID, Date timestamp) {
        this.baggageID = baggageID;
        this.conveyorID = conveyorID;
        this.timestamp = timestamp;
    }

    public int getBaggageID() {
        return baggageID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
