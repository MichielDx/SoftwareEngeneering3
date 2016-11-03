package main.be.kdg.bagageafhandeling.transport.engine;

import main.be.kdg.bagageafhandeling.transport.model.Bagage;
import main.be.kdg.bagageafhandeling.transport.model.TimePeriod;
import main.be.kdg.bagageafhandeling.transport.services.*;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
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
        this.timePeriod = timePeriod;
        bagageOutput = new BagageOutput();
    }

    public BagageScheduler(TimePeriod timePeriod, String recordPath, RecordOption option) {
        flightIdGen = new FlightIdGeneratorImpl();
        conveyerIdGen = new ConveyerIdGeneratorImpl();
        sensorIdGen = new SensorIdGeneratorImpl();
        this.timePeriod = timePeriod;

        if (option == RecordOption.JSON) bagageOutput = new BagageOutput(recordPath,new BagageJsonService());
        else bagageOutput = new BagageOutput(recordPath,new BagageXmlService());
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        while(true) {
            bagage = new Bagage(flightIdGen.getId(), conveyerIdGen.getId(), sensorIdGen.getId());
            bagageOutput.publish(bagage);
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
