package main.be.kdg.bagageafhandeling.transport.repository;

import main.be.kdg.bagageafhandeling.transport.exceptions.RepositoryException;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Arthur Haelterman on 4/11/2016.
 */
public class BaggageRepository {
    private ConcurrentMap<Integer,Baggage> baggages;

    public BaggageRepository() {
        baggages = new ConcurrentHashMap<>();
    }

    public void addBagage(Baggage baggage) {
        baggages.put(baggage.getBaggageID(),baggage);
    }

    public void updateBagage(Baggage baggage) {
        baggages.replace(baggage.getBaggageID(), baggage);
    }

    public Baggage getBaggage(int baggageId) throws RepositoryException {
        Baggage result = baggages.get(baggageId);
        if (result == null) throw new RepositoryException("Baggage with ID " + baggageId + " not found in cache.");
        return result;
    }
}
