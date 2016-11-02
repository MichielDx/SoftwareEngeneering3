package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.model.Bagage;
import main.be.kdg.bagageafhandeling.transport.model.FrequencySchedule;
import main.be.kdg.bagageafhandeling.transport.model.TimePeriod;
import org.apache.log4j.Logger;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BagageScheduler implements Runnable, Observer{
    private IdGeneratorService flightIdGen;
    private IdGeneratorService conveyerIdGen;
    private IdGeneratorService sensorIdGen;
    private TimePeriod timePeriod;
    private Bagage bagage;
    private BagageOutput bagageOutput;
    private Logger logger = Logger.getLogger(BagageScheduler.class);

    public BagageScheduler(TimePeriod timePeriod) {
        flightIdGen = new FlightIdGeneratorImpl();
        conveyerIdGen = new ConveyerIdGeneratorImpl();
        sensorIdGen = new SensorIdGeneratorImpl();
        bagageOutput = new BagageOutput();
        this.timePeriod = timePeriod;
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        while(true) {
            bagage = new Bagage(flightIdGen.getId(), conveyerIdGen.getId(), sensorIdGen.getId());
            bagageOutput.publishMessage(bagage);
            logger.info(String.format("Created bagage with ID %d at %s", bagage.getBagageID(), sdf.format(bagage.getTimestamp())));
            try {
                Thread.sleep(timePeriod.getFrequency());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.timePeriod = ((TimePeriod)arg);
    }
}
