package main.be.kdg.bagageafhandeling.transport;

import main.be.kdg.bagageafhandeling.transport.model.FrequencySchedule;
import main.be.kdg.bagageafhandeling.transport.model.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.model.TimePeriod;
import main.be.kdg.bagageafhandeling.transport.engine.BagageScheduler;
import main.be.kdg.bagageafhandeling.transport.model.FormatOption;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;

/**
 * Created by Michiel on 2/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        String log4jConfPath = "C:\\Users\\Michiel\\SoftwareEngineering3\\src\\main\\log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        FrequencySchedule f = getFrequencySchedule();

        //DayScheduler dayScheduler = new DayScheduler(f);
        BagageScheduler bagageScheduler = new BagageScheduler(f.getCurrentTimePeriod(), "C:\\Users\\Michiel\\Desktop\\test.json", FormatOption.JSON, SimulatorMode.REPLAY);
        //dayScheduler.addObserver(bagageScheduler);

        //Thread day = new Thread(dayScheduler);
        Thread bagage = new Thread(bagageScheduler);
        //day.start();
        bagage.start();
    }

    private static FrequencySchedule getFrequencySchedule(){
        ArrayList<TimePeriod> periods = new ArrayList<>();
        periods.add(new TimePeriod(0, 2, 5000));
        periods.add(new TimePeriod(2, 6, 10000));
        periods.add(new TimePeriod(6, 12, 3000));
        periods.add(new TimePeriod(12, 16, 2000));
        periods.add(new TimePeriod(16, 20, 1000));
        periods.add(new TimePeriod(20, 24, 4000));
        return new FrequencySchedule(periods);
    }
}
