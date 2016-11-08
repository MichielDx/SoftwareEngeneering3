package main;

import main.be.kdg.bagageafhandeling.transport.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.transport.adapters.in.RabbitMQIn;
import main.be.kdg.bagageafhandeling.transport.adapters.out.RabbitMQOut;
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
        controller.setRecordPath("C:\\Users\\Michiel\\Desktop\\test.json");
        controller.setRecord(false);
        controller.setMode(SimulatorMode.REPLAY);
        controller.setOption(FormatOption.JSON);
        controller.setRouteInputQueue(new RabbitMQIn("routeQueue"));
        controller.setConveyorService(new ConveyorServiceAPI());
        controller.setMethod(DelayMethod.CALCULATED);
        controller.setSensorOutputQueue(new RabbitMQOut("sensorQueue"));
        controller.setBaggageOutputQueue(new RabbitMQOut("baggageQueue"));
        controller.setClearCacheTime(21600000);
        controller.initialize();
        controller.start();
    }

}
