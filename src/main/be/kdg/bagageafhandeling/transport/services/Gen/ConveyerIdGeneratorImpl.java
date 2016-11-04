package main.be.kdg.bagageafhandeling.transport.services.Gen;

import main.be.kdg.bagageafhandeling.transport.services.Interface.IdGeneratorService;

import java.util.Random;

/**
 * Created by Michiel on 2/11/2016.
 */
public class ConveyerIdGeneratorImpl implements IdGeneratorService {
    private static Random rnd = new Random();
    private static final int[] conveyerIDs = new int[]{15, 10, 39, 70, 71, 48, 99, 96, 19, 46};

    @Override
    public int getId() {
        int index = rnd.nextInt(conveyerIDs.length);
        return conveyerIDs[index];
    }
}
