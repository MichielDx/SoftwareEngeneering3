package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Arthur Haelterman on 4/11/2016.
 */
public class BaggageRepository {
    private static ConcurrentMap<Integer,Baggage> baggages;

    public BaggageRepository() {
        baggages = new ConcurrentHashMap<>();
    }

    public void addBagage(Baggage baggage) {
        baggages.put(baggage.getBaggageID(),baggage);
    }

    public static void updateBagage(Baggage baggage) {
        baggages.replace(baggage.getBaggageID(), baggage);
    }

    public static Baggage getBagage(int baggageID) {
        Baggage temp = baggages.get(baggageID);
        return temp;
    }
}
