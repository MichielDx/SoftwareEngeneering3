package main.be.kdg.bagageafhandeling.transport.models.conveyor;

/**
 * Created by Michiel on 4/11/2016.
 */
public class Segment {
    private int inPoint;
    private int outPoint;
    private int distance;

    public int getInPoint() {
        return inPoint;
    }

    public int getOutPoint() {
        return outPoint;
    }

    public int getDistance() {
        return distance;
    }

    public void setInPoint(int inPoint) {
        this.inPoint = inPoint;
    }

    public void setOutPoint(int outPoint) {
        this.outPoint = outPoint;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
