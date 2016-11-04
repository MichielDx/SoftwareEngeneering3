package main.be.kdg.bagageafhandeling.transport.engine;

import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;
import main.be.kdg.bagageafhandeling.transport.model.*;
import main.be.kdg.bagageafhandeling.transport.model.Bagage.Bagage;
import main.be.kdg.bagageafhandeling.transport.model.DTO.BagageRecordDTO;
import main.be.kdg.bagageafhandeling.transport.model.Enum.FormatOption;
import main.be.kdg.bagageafhandeling.transport.model.Enum.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.services.Bagage.BagageInput;
import main.be.kdg.bagageafhandeling.transport.services.Bagage.BagageJsonService;
import main.be.kdg.bagageafhandeling.transport.services.Bagage.BagageOutput;
import main.be.kdg.bagageafhandeling.transport.services.Bagage.BagageXmlService;
import main.be.kdg.bagageafhandeling.transport.services.Gen.ConveyerIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.Gen.FlightIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.Gen.SensorIdGeneratorImpl;
import main.be.kdg.bagageafhandeling.transport.services.Interface.IdGeneratorService;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BagageScheduler implements Runnable, Observer {
    private IdGeneratorService flightIdGen;
    private IdGeneratorService conveyerIdGen;
    private IdGeneratorService sensorIdGen;
    private TimePeriod timePeriod;
    private Bagage bagage;
    private long frequency;
    private BagageOutput bagageOutput;
    private BagageInput bagageInput;
    private SimulatorMode mode;
    private volatile boolean running = true;
    private Logger logger = Logger.getLogger(BagageScheduler.class);

    public BagageScheduler(TimePeriod timePeriod) {
        flightIdGen = new FlightIdGeneratorImpl();
        conveyerIdGen = new ConveyerIdGeneratorImpl();
        sensorIdGen = new SensorIdGeneratorImpl();
        this.timePeriod = timePeriod;
        bagageOutput = new BagageOutput();
    }

    public BagageScheduler(TimePeriod timePeriod, String recordPath, FormatOption option, SimulatorMode mode) {
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
            bagageOutput = new BagageOutput(recordPath, new BagageJsonService(), true);
        else bagageOutput = new BagageOutput(recordPath, new BagageXmlService(), true);
    }

    private void initializeReplay(String recordPath, FormatOption formatOption) {
        if (formatOption == FormatOption.JSON) {
            bagageInput = new BagageInput(recordPath, new BagageJsonService());
        } else {
            bagageInput = new BagageInput(recordPath, new BagageXmlService());
        }
        bagageOutput = new BagageOutput();
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        while (running) {
            try {
                setParameters();
                bagageOutput.publish(bagage);
                logger.info(String.format("Created bagage with ID %d at %s", bagage.getBagageID(), sdf.format(bagage.getTimestamp())));
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
        System.exit(0);
    }

    private void setParameters() throws EndReplayException {
        if (mode == SimulatorMode.GENERATION) {
            bagage = new Bagage(flightIdGen.getId(), conveyerIdGen.getId(), sensorIdGen.getId());
            frequency = timePeriod.getFrequency();
        } else {
            BagageRecordDTO bagageRecordDTO = bagageInput.getNextBagage();
            bagage = bagageRecordDTO.getBagage();
            frequency = bagageRecordDTO.getFrequency();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            bagageOutput.write();
        } else {
            this.timePeriod = ((TimePeriod) arg);
        }
    }
}
