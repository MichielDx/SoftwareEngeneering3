package main.be.kdg.bagageafhandeling.transport.models.conveyor;

/**
 * Created by Michiel on 4/11/2016.
 */
public class Connector {
    private int connectedConveyorID;
    private String type;
    private int length;
    private int speed;

    public int getConnectedConveyorID() {
        return connectedConveyorID;
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public int getSpeed() {
        return speed;
    }

    public void setConnectedConveyorID(int connectedConveyorID) {
        this.connectedConveyorID = connectedConveyorID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
