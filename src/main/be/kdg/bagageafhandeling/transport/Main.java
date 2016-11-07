package main.be.kdg.bagageafhandeling.transport;

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

        controller.setRecordPath("C:\\Users\\Arthur Haelterman\\Desktop\\test.json");

        controller.setMode(SimulatorMode.GENERATION);
        controller.setOption(FormatOption.JSON);

        controller.setRouteInputQueue(new RabbitMQIn("routeQueue"));
        controller.setConveyorService(new ConveyorServiceAPI());
        controller.setMethod(DelayMethod.CALCULATED);

        controller.setSensorOutputQueue(new RabbitMQOut("sensorQueue"));
        controller.setBaggageOutputQueue(new RabbitMQOut("baggageQueue"));

        controller.initialize();

        controller.start();
    }

}
