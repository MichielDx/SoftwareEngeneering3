package main.be.kdg.bagageafhandeling.transport.engines;

import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;
import main.be.kdg.bagageafhandeling.transport.models.TimePeriod;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageRecordDTO;
import main.be.kdg.bagageafhandeling.transport.models.enums.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.repository.BaggageRepository;
import main.be.kdg.bagageafhandeling.transport.services.Publisher;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageReader;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageRecorder;
import main.be.kdg.bagageafhandeling.transport.services.gen.ConveyerIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.gen.FlightIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.gen.SensorIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.IdGeneratorService;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * a Baggagescheduler creates a {@link Baggage} object and publishes this to a message broker periodically.
 *
 * If this class is started in {@link SimulatorMode} 'Generation' all {@link Baggage}
 * Objects to be published are created with generated ID's.
 * The frequency of creating and publishing objects is determined by a {@link TimePeriod}
 * This class observes {@link DayScheduler} to periodically receive a new {@link TimePeriod}
 *
 * If this class is started in {@link SimulatorMode} 'Replay' all {@link Baggage}.
 * An Object to be published is created by converting it from a {@link BaggageRecordDTO} object from a in-memory cache read in from a file during startup.
 * The frequency of publishing objects is determined by the frequency of the current {@link BaggageRecordDTO} object
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
    private BaggageRepository baggageRepository;

    public BaggageScheduler(BaggageRepository baggageRepository, Publisher baggagePublisher, TimePeriod timePeriod, SimulatorMode mode, BaggageRecorder baggageRecorder, BaggageReader baggageReader) {
        flightIdGen = new FlightIdGeneratorImpl();
        conveyerIdGen = new ConveyerIdGeneratorImpl();
        sensorIdGen = new SensorIdGeneratorImpl();
        this.baggageRepository = baggageRepository;
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
                if (baggageRecorder != null) baggageRecorder.record(baggage);
                baggageRepository.addBagage(baggage);
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

    /**
     * If the argument is null and the {@link BaggageRecorder} is not null, the baggageRecorder will write the recorded baggages to a file.
     * Otherwise the previous {@link TimePeriod} gets overwritten by the current timeperiod.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            if (baggageRecorder != null) baggageRecorder.write();
        } else {
            this.timePeriod = ((TimePeriod) arg);
        }
    }
}
