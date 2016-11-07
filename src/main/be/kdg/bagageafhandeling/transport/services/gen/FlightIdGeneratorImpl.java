package main.be.kdg.bagageafhandeling.transport.services.gen;

import main.be.kdg.bagageafhandeling.transport.services.interfaces.IdGeneratorService;

import java.util.Random;

/**
 * Created by Michiel on 2/11/2016.
 */
public class FlightIdGeneratorImpl implements IdGeneratorService {
    private static Random rnd = new Random();
    private static final int[] flightIDs = new int[]{7485528, 9418996, 5052024, 6742152, 6811661, 2694053, 9029477, 6818339, 6233935, 5407052, 9580464, 4840697, 3512223, 2627010, 6661074, 7137070, 5743437, 3410078, 2323852, 8042453};
    //private static final int[] flightIDs = new int[]{1111111,1111112};

    @Override
    public int getId() {
        int index = rnd.nextInt(flightIDs.length);
        return flightIDs[index];
    }
}
