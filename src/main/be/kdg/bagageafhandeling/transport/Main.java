package main.be.kdg.bagageafhandeling.transport;

import main.be.kdg.bagageafhandeling.transport.Controller.Controller;
import main.be.kdg.bagageafhandeling.transport.engine.RouteScheduler;
import main.be.kdg.bagageafhandeling.transport.model.Enum.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.model.FrequencySchedule;
import main.be.kdg.bagageafhandeling.transport.model.Enum.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.model.TimePeriod;
import main.be.kdg.bagageafhandeling.transport.engine.BagageScheduler;
import main.be.kdg.bagageafhandeling.transport.model.Enum.FormatOption;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;

/**
 * Created by Michiel on 2/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setMode(SimulatorMode.GENERATION);
        controller.setOption(FormatOption.JSON);
        controller.setRecordPath("C:\\Users\\Arthur Haelterman\\Desktop\\test.json");
        controller.setMethod(DelayMethod.CALCULATED);
        controller.initialize();
        controller.start();
    }

}
