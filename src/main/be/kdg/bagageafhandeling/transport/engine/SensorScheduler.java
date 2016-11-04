package main.be.kdg.bagageafhandeling.transport.engine;

import main.be.kdg.bagageafhandeling.transport.model.Conveyor;
import main.be.kdg.bagageafhandeling.transport.model.DTO.BagageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.model.Enum.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.services.RouteInput;
import main.be.kdg.bagageafhandeling.transport.services.RouteOutput;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michiel on 4/11/2016.
 */
public class SensorScheduler implements Runnable {
    private static List<BagageMessageDTO> bagageMessageDTOs = new LinkedList<>();
    private RouteOutput routeOutput;
    private RouteInput routeInput;
    private DelayMethod delayMethod;
    private Conveyor conveyor;
    private long delay;
    private Logger logger = Logger.getLogger(SensorScheduler.class);

    public SensorScheduler(DelayMethod delayMethod, long delay) {
        this.routeInput = new RouteInput();
        this.routeOutput = new RouteOutput();
        this.delayMethod = delayMethod;
        this.delay = delay;
    }

    public static void addBagageMessageDTO(BagageMessageDTO message) {
        bagageMessageDTOs.add(message);
    }

    @Override
    public void run() {
        while (true) {
            if (bagageMessageDTOs.size() != 0) {
                doTask();
            }
        }
    }

    private void doTask(){
        for (BagageMessageDTO bagageMessageDTO : bagageMessageDTOs) {
            conveyor = routeInput.getConveyor(bagageMessageDTO.getConveyorID());
            logger.info("Conveyor received");
            break;
        }
    }
}
