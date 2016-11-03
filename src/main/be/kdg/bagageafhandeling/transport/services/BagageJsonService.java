package main.be.kdg.bagageafhandeling.transport.services;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.be.kdg.bagageafhandeling.transport.model.BagageRecordList;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class BagageJsonService implements BagageConversionService {
    Gson gson;

    public BagageJsonService(){
        gson = new GsonBuilder().setPrettyPrinting().create();

    }
    public String serializeAll (BagageRecordList bagages) {
        return gson.toJson(bagages);
    }

}
