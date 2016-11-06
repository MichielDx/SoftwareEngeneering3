package main.be.kdg.bagageafhandeling.transport.services.route;

import main.be.kdg.bagageafhandeling.transport.models.conveyor.Conveyor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Arthur Haelterman on 4/11/2016.
 */
public class ConveyorRepository {
    private static ConcurrentMap<Integer,Conveyor> conveyorCache;

    public ConveyorRepository() {
        conveyorCache = new ConcurrentHashMap<>();
    }

    public static void addConveyor(Conveyor conveyor){
        conveyorCache.put(conveyor.getConveyorID(),conveyor);
    }

    public boolean contains(int key){
        return conveyorCache.containsKey(key);
    }

    public void clearCache(){
        conveyorCache.clear();
    }

    public Conveyor getConveyor(int key){
        return conveyorCache.get(key);
    }

}
