package main.be.kdg.bagageafhandeling.transport.adapters.in;

import be.kdg.se3.proxy.ConveyorServiceProxy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.be.kdg.bagageafhandeling.transport.exceptions.APIException;
import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.ConveyorService;
import retrofit2.Retrofit;
import retrofit2.http.Path;

import java.io.IOException;

/**
 * Created by Michiel on 4/11/2016.
 */
public class ConveyorServiceAPI implements ConveyorService {
    private ConveyorService conveyorService;
    private Retrofit retrofit;
    private Conveyor conveyor;
    private ConveyorServiceProxy conveyorServiceProxy;
    private Gson gson;

    public ConveyorServiceAPI() {
        conveyorServiceProxy = new be.kdg.se3.proxy.ConveyorServiceProxy();
        gson = new Gson();
       /* retrofit = new Retrofit.Builder().baseUrl("http://www.services4se3.com/")
                .build();
        conveyorService = retrofit.create(ConveyorService.class);*/
    }

    @Override
    public Conveyor fetchConveyor(@Path("conveyorID") int conveyorId) throws APIException {
        //conveyor = conveyorService.fetchConveyor(conveyorId);
        try {
            String result = conveyorServiceProxy.get("www.services4se3.com/conveyorservice/" + conveyorId);
            conveyor = gson.fromJson(result, Conveyor.class);
        } catch (IOException | JsonSyntaxException e ) {
            throw new APIException("Error when trying to convert from json to conveyor", e);
        }
        return conveyor;
    }
}
