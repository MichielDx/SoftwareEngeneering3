package main.be.kdg.bagageafhandeling.transport.services.gen;

import main.be.kdg.bagageafhandeling.transport.services.interfaces.IdGeneratorService;

import java.util.Random;

/**
 * Created by Michiel on 2/11/2016.
 */
public class FlightIdGeneratorImpl implements IdGeneratorService {
    private static Random rnd = new Random();
    private static final int[] flightIDs = new int[]{748552, 941899, 505202, 674215, 681166, 269405, 902947, 681833, 623393, 540705, 958046, 484069, 351222, 262701, 666107, 713707, 574343, 341007, 232385, 804245};

    @Override
    public int getId() {
        int index = rnd.nextInt(flightIDs.length);
        return flightIDs[index];
    }
}
