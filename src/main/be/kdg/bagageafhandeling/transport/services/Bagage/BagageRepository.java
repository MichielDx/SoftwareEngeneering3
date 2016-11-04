package main.be.kdg.bagageafhandeling.transport.services.Bagage;

import main.be.kdg.bagageafhandeling.transport.model.Bagage.Bagage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Haelterman on 4/11/2016.
 */
public class BagageRepository {
    private static List<Bagage> bagageList;

    public BagageRepository() {
        bagageList = new ArrayList<>();
    }

    public void addBagage(Bagage bagage) {
        bagageList.add(bagage);
    }

    public static void updateBagage(Bagage bagage) {
        bagageList.set(bagage.getBagageID(), bagage);
    }

    public static Bagage getBagage(int bagageID) {
        return bagageList.get(bagageID);
    }
}
