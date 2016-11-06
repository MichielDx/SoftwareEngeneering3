package main.be.kdg.bagageafhandeling.transport.engines;

import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;
import main.be.kdg.bagageafhandeling.transport.models.*;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageRecordDTO;
import main.be.kdg.bagageafhandeling.transport.models.enums.FormatOption;
import main.be.kdg.bagageafhandeling.transport.models.enums.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageInput;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageJsonService;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageOutput;
import main.be.kdg.bagageafhandeling.transport.services.bagage.BaggageXmlService;
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
    private BaggageOutput baggageOutput;
    private BaggageInput baggageInput;
    private SimulatorMode mode;
    private volatile boolean running = true;
    private Logger logger = Logger.getLogger(BaggageScheduler.class);

    public BaggageScheduler(TimePeriod timePeriod) {
        flightIdGen = new FlightIdGeneratorImpl();
        conveyerIdGen = new ConveyerIdGeneratorImpl();
        sensorIdGen = new SensorIdGeneratorImpl();
        this.timePeriod = timePeriod;
        baggageOutput = new BaggageOutput();
    }

    public BaggageScheduler(TimePeriod timePeriod, String recordPath, FormatOption option, SimulatorMode mode) {
        flightIdGen = new FlightIdGeneratorImpl();
        conveyerIdGen = new ConveyerIdGeneratorImpl();
        sensorIdGen = new SensorIdGeneratorImpl();
        this.timePeriod = timePeriod;
        this.mode = mode;
        if (this.mode == SimulatorMode.GENERATION) {
            initializeGeneration(recordPath, option);
        } else {
            initializeReplay(recordPath, option);
        }
    }

    private void initializeGeneration(String recordPath, FormatOption formatOption) {
        if (formatOption == FormatOption.JSON)
            baggageOutput = new BaggageOutput(recordPath, new BaggageJsonService(), true);
        else baggageOutput = new BaggageOutput(recordPath, new BaggageXmlService(), true);
    }

    private void initializeReplay(String recordPath, FormatOption formatOption) {
        if (formatOption == FormatOption.JSON) {
            baggageInput = new BaggageInput(recordPath, new BaggageJsonService());
        } else {
            baggageInput = new BaggageInput(recordPath, new BaggageXmlService());
        }
        baggageOutput = new BaggageOutput();
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        while (running) {
            try {
                setParameters();
                baggageOutput.publish(baggage);
                logger.info(String.format("Created baggage with ID %d at %s", baggage.getBaggageID(), sdf.format(baggage.getTimestamp())));
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
            BaggageRecordDTO baggageRecordDTO = baggageInput.getNextBagage();
            baggage = baggageRecordDTO.getBaggage();
            frequency = baggageRecordDTO.getFrequency();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            baggageOutput.write();
        } else {
            this.timePeriod = ((TimePeriod) arg);
        }
    }
}
