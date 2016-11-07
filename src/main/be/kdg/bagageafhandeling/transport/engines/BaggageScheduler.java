package main.be.kdg.bagageafhandeling.transport.engines;

import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;
import main.be.kdg.bagageafhandeling.transport.models.*;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageRecordDTO;
import main.be.kdg.bagageafhandeling.transport.models.enums.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.services.Publisher;
import main.be.kdg.bagageafhandeling.transport.services.bagage.*;
import main.be.kdg.bagageafhandeling.transport.services.gen.ConveyerIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.gen.FlightIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.gen.SensorIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.IdGeneratorService;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BaggageScheduler implements Runnable, Observer {
    private IdGeneratorService flightIdGen;
    private IdGeneratorService conveyerIdGen;
    private IdGeneratorService sensorIdGen;
    private TimePeriod timePeriod;
    private Baggage baggage;
    private long frequency;
    private BaggageReader baggageReader;
    private BaggageRecorder baggageRecorder;
    private SimulatorMode mode;
    private volatile boolean running = true;
    private Logger logger = Logger.getLogger(BaggageScheduler.class);
    private Publisher baggagePublisher;

    public BaggageScheduler(Publisher baggagePublisher, TimePeriod timePeriod, SimulatorMode mode, BaggageRecorder baggageRecorder, BaggageReader baggageReader) {
        flightIdGen = new FlightIdGeneratorImpl();
        conveyerIdGen = new ConveyerIdGeneratorImpl();
        sensorIdGen = new SensorIdGeneratorImpl();
        this.timePeriod = timePeriod;
        this.mode = mode;
        this.baggageRecorder = baggageRecorder;
        this.baggageReader = baggageReader;
        this.baggagePublisher = baggagePublisher;
    }


    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        while (running) {
            try {
                setParameters();
                baggagePublisher.publish(baggage);
                if (mode == SimulatorMode.GENERATION) baggageRecorder.record(baggage);
                BaggageRepository.addBagage(baggage);
                logger.info(String.format("Created and published baggage with ID %d at %s", baggage.getBaggageID(), sdf.format(baggage.getTimestamp())));
                try {
                    Thread.sleep(frequency);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            } catch (EndReplayException e) {
                logger.info(e.getMessage());
                running = false;

            }
        }
        //System.exit(0);
    }

    private void setParameters() throws EndReplayException {
        if (mode == SimulatorMode.GENERATION) {
            baggage = new Baggage(flightIdGen.getId(), conveyerIdGen.getId(), sensorIdGen.getId());
            frequency = timePeriod.getFrequency();
        } else {
            BaggageRecordDTO baggageRecordDTO = baggageReader.getNextBagage();
            baggage = baggageRecordDTO.getBaggage();
            frequency = baggageRecordDTO.getFrequency();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
           if (mode == SimulatorMode.GENERATION) baggageRecorder.write();
        } else {
            this.timePeriod = ((TimePeriod) arg);
        }
    }
}
