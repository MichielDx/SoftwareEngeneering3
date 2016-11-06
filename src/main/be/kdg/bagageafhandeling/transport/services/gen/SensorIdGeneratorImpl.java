package main.be.kdg.bagageafhandeling.transport.services.gen;

import main.be.kdg.bagageafhandeling.transport.services.interfaces.IdGeneratorService;

import java.util.Random;

/**
 * Created by Michiel on 2/11/2016.
 */
public class SensorIdGeneratorImpl implements IdGeneratorService {
    private static Random rnd = new Random();
    private static final int[] sensorIds = new int[]{1,2,3,11,12,21,22,42,31,41};

    @Override
    public int getId() {
        int index = rnd.nextInt(sensorIds.length);
        return sensorIds[index];
    }
}
