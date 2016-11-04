package main.be.kdg.bagageafhandeling.transport.model.Conveyor;

import java.util.List;

/**
 * Created by Michiel on 4/11/2016.
 */
public class Conveyor {
    private int conveyorId;
    private int length;
    private int speed;
    private List<Connector> connectors;
    private List<Segment> segment;
}
