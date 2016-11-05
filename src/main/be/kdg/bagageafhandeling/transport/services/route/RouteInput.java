package main.be.kdg.bagageafhandeling.transport.services.route;

import main.be.kdg.bagageafhandeling.transport.adapters.in.RabbitMQ;
import main.be.kdg.bagageafhandeling.transport.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageInputService;
import org.apache.log4j.Logger;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class RouteInput {
    private Logger logger = Logger.getLogger(RouteInput.class);
    private MessageInputService rabbitMQ;
    private ConveyorService conveyorService;

    public RouteInput() {

    }

    public void initializeRabbitMQ(RouteScheduler routeScheduler) throws MessageInputException {
        rabbitMQ = new RabbitMQ("routeInputQueue");
        rabbitMQ.initialize();
        rabbitMQ.addObserver(routeScheduler);
        rabbitMQ.retrieve();
    }

    public void initializeAPI(ConveyorService conveyorService){
        this.conveyorService = conveyorService;
    }

    public Conveyor getConveyor(int conveyorId) throws APIException{
        return conveyorService.fetchConveyor(conveyorId);

    }
}
