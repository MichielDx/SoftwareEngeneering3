package main.be.kdg.bagageafhandeling.transport.services.bagage;


import com.google.gson.Gson;
import main.be.kdg.bagageafhandeling.transport.models.baggage.BaggageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.BaggageConversionService;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class BaggageJsonService implements BaggageConversionService {
    Gson gson;

    public BaggageJsonService(){
        gson = new Gson();
    }

    @Override
    public String serializeAll (BaggageRecordList bagages) {
        return gson.toJson(bagages);
    }

    @Override
    public BaggageRecordList deserializeAll(String string) {
        return gson.fromJson(string,BaggageRecordList.class);
    }


}
