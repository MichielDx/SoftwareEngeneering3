package main.be.kdg.bagageafhandeling.transport.services.Route;

import main.be.kdg.bagageafhandeling.transport.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.exceptions.RecordWriterException;
import main.be.kdg.bagageafhandeling.transport.model.Bagage.Bagage;
import main.be.kdg.bagageafhandeling.transport.model.Conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.model.SensorMessage;
import org.apache.log4j.Logger;


/**
 * Created by Michiel on 4/11/2016.
 */
public class RouteOutput {
    private RabbitMQ rabbitMQ;
    private Logger logger = Logger.getLogger(RouteOutput.class);
    private RouteXmlService routeXmlService;

    public RouteOutput() {
        rabbitMQ = new RabbitMQ("sensorOutputQueue");
        routeXmlService = new RouteXmlService();
        initialize();
    }

    private void initialize() {
        try {
            rabbitMQ.initialize();
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void publish(SensorMessage sensorMessage) {
        try {
            rabbitMQ.publish(routeXmlService.serialize(sensorMessage));
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }
}
