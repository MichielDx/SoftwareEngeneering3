package main.be.kdg.bagageafhandeling.transport.services.Gen;

import main.be.kdg.bagageafhandeling.transport.services.Interface.IdGeneratorService;

import java.util.Random;

/**
 * Created by Michiel on 2/11/2016.
 */
public class SensorIdGeneratorImpl implements IdGeneratorService {
    private static Random rnd = new Random();
    private static final int[] sensorIds = new int[]{1,2,3,4,5};


    @Override
    public int getId() {
        int index = rnd.nextInt(sensorIds.length);
        return sensorIds[index];
    }
}
