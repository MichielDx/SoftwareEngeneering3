package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.model.Conveyor;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * Created by Michiel on 4/11/2016.
 */
public interface ConveyorService {
    @GET("/conveyorservice/{conveyorID}")
    public Conveyor fetchConveyor(@Path("conveyorID") int conveyorId);
}
