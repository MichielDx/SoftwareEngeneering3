package main.be.kdg.bagageafhandeling.transport.models.conveyor;

import java.util.List;

/**
 * Created by Michiel on 4/11/2016.
 */
public class Conveyor {
    private int conveyorID;
    private int length;
    private int speed;
    private List<Connector> connectors;
    private List<Segment> segments;

    public Conveyor(int conveyorID, int length, int speed, List<Connector> connectors, List<Segment> segments) {
        this.conveyorID = conveyorID;
        this.length = length;
        this.speed = speed;
        this.connectors = connectors;
        this.segments = segments;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public int getLength() {
        return length;
    }

    public int getSpeed() {
        return speed;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
