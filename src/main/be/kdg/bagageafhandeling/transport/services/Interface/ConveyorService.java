package main.be.kdg.bagageafhandeling.transport.services.Interface;

import main.be.kdg.bagageafhandeling.transport.model.Conveyor.Conveyor;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Michiel on 4/11/2016.
 */
public interface ConveyorService {
    @GET("/conveyorservice/{conveyorID}")
    public Conveyor fetchConveyor(@Path("conveyorID") int conveyorId);
}
