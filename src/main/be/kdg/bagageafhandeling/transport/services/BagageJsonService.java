package main.be.kdg.bagageafhandeling.transport.services;


import com.google.gson.Gson;
import main.be.kdg.bagageafhandeling.transport.model.Bagage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public class BagageJsonService implements BagageConversionService {
    Gson gson;

    public BagageJsonService(){
        gson = new Gson();

    }
    public String serialize (Bagage bagage) {
        return gson.toJson(bagage);
    }

}
