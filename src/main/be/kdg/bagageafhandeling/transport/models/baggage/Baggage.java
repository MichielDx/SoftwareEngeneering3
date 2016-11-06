package main.be.kdg.bagageafhandeling.transport.models.baggage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Michiel on 2/11/2016.
 */
@XmlRootElement
public class Baggage {
    private int bagageID;
    private int flightID;
    private int conveyorID;
    private int sensorID;
    private Date timestamp;
    private static final AtomicInteger count = new AtomicInteger(0);

    public Baggage() {
    }

    public Baggage(int flightID, int conveyorID, int sensorID) {
        this.bagageID = count.incrementAndGet();
        this.flightID = flightID;
        this.conveyorID = conveyorID;
        this.sensorID = sensorID;
        this.timestamp = new Date();
    }
    @XmlElement
    public int getBaggageID() {
        return bagageID;
    }
    @XmlElement
    public int getFlightID() {
        return flightID;
    }
    @XmlElement
    public int getConveyorID() {
        return conveyorID;
    }
    @XmlElement
    public int getSensorID() {
        return sensorID;
    }
    @XmlElement
    public Date getTimestamp() {
        return timestamp;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }
}
