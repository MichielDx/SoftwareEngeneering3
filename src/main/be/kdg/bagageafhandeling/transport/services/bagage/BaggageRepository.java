package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Haelterman on 4/11/2016.
 */
public class BaggageRepository {
    private static List<Baggage> baggageList;

    public BaggageRepository() {
        baggageList = new ArrayList<>();
    }

    public synchronized void addBagage(Baggage baggage) {
        baggageList.add(baggage);
    }

    public synchronized static void updateBagage(Baggage baggage) {
        baggageList.set(baggage.getBaggageID(), baggage);
    }

    public static Baggage getBagage(int bagageID) {
        return baggageList.get(bagageID);
    }
}
