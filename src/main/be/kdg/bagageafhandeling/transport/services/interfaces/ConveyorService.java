package main.be.kdg.bagageafhandeling.transport.services.interfaces;

import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * A ConveyorService fetches a {@link Conveyor} from a remote host
 */
public interface ConveyorService {
    @GET("/conveyorservice/{conveyorID}")
    public Conveyor fetchConveyor(@Path("conveyorID") int conveyorId) throws APIException;
}
