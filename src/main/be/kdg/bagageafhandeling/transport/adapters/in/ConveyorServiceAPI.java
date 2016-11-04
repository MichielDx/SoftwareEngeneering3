package main.be.kdg.bagageafhandeling.transport.adapters.in;

import be.kdg.se3.proxy.ConveyorServiceProxy;
import com.google.gson.Gson;
import main.be.kdg.bagageafhandeling.transport.model.Conveyor.Conveyor;
import main.be.kdg.bagageafhandeling.transport.services.Interface.ConveyorService;
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
        conveyorServiceProxy = new ConveyorServiceProxy();
        gson = new Gson();
       /* retrofit = new Retrofit.Builder().baseUrl("http://www.services4se3.com/")
                .build();
        conveyorService = retrofit.create(ConveyorService.class);*/
    }

    @Override
    public Conveyor fetchConveyor(@Path("conveyorID") int conveyorId) {
        //conveyor = conveyorService.fetchConveyor(conveyorId);
        try {
            conveyor = gson.fromJson(conveyorServiceProxy.get("http://www.services4se3.com/conveyorservice/" + conveyorId), Conveyor.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conveyor;
    }
}
