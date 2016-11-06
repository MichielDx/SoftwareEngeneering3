package main.be.kdg.bagageafhandeling.transport.services.route;

import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageInputService;
import org.apache.log4j.Logger;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class ConveyorAPI {
    private Logger logger = Logger.getLogger(ConveyorAPI.class);
    private ConveyorService conveyorService;

    public ConveyorAPI(ConveyorService conveyorService) {
        this.conveyorService = conveyorService;
    }

    public Conveyor getConveyor(int conveyorId) throws APIException{
        return conveyorService.fetchConveyor(conveyorId);

    }
}
