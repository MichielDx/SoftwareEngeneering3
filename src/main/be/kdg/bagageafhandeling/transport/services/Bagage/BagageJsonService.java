package main.be.kdg.bagageafhandeling.transport.services.Bagage;


import com.google.gson.Gson;
import main.be.kdg.bagageafhandeling.transport.model.Bagage.BagageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.Interface.BagageConversionService;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class BagageJsonService implements BagageConversionService {
    Gson gson;

    public BagageJsonService(){
        gson = new Gson();
    }

    @Override
    public String serializeAll (BagageRecordList bagages) {
        return gson.toJson(bagages);
    }

    @Override
    public BagageRecordList deserializeAll(String string) {
        return gson.fromJson(string,BagageRecordList.class);
    }


}
