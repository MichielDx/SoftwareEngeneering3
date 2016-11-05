package main.be.kdg.bagageafhandeling.transport;

import main.be.kdg.bagageafhandeling.transport.controllers.Controller;
import main.be.kdg.bagageafhandeling.transport.models.enums.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.models.enums.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.models.enums.FormatOption;

/**
 * Created by Michiel on 2/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setMode(SimulatorMode.GENERATION);
        controller.setOption(FormatOption.JSON);
        controller.setRecordPath("C:\\Users\\Michiel\\Desktop\\test.json");
        controller.setMethod(DelayMethod.CALCULATED);
        controller.initialize();
        controller.start();
    }

}
