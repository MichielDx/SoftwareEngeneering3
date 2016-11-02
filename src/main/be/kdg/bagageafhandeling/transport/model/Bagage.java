package main.be.kdg.bagageafhandeling.transport.model;

import main.be.kdg.bagageafhandeling.transport.services.ConveyerIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.FlightIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.IdGeneratorService;
import main.be.kdg.bagageafhandeling.transport.services.SensorIdGeneratorImpl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Michiel on 2/11/2016.
 */
@XmlRootElement
public class Bagage {
    private int bagageID;
    private int flightID;
    private int conveyerID;
    private int sensorID;
    private Date timestamp;
    private static final AtomicInteger count = new AtomicInteger(0);

    public Bagage() {
    }

    public Bagage(int flightID, int conveyerID, int sensorID) {
        this.bagageID = count.incrementAndGet();
        this.flightID = flightID;
        this.conveyerID = conveyerID;
        this.sensorID = sensorID;
        this.timestamp = new Date();
    }
    @XmlElement
    public int getBagageID() {
        return bagageID;
    }
    @XmlElement
    public int getFlightID() {
        return flightID;
    }
    @XmlElement
    public int getConveyerID() {
        return conveyerID;
    }
    @XmlElement
    public int getSensorID() {
        return sensorID;
    }
    @XmlElement
    public Date getTimestamp() {
        return timestamp;
    }
}
