package main.be.kdg.bagageafhandeling.transport;

import main.be.kdg.bagageafhandeling.transport.model.FrequencySchedule;
import main.be.kdg.bagageafhandeling.transport.model.TimePeriod;
import main.be.kdg.bagageafhandeling.transport.engine.BagageScheduler;
import main.be.kdg.bagageafhandeling.transport.engine.DayScheduler;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michiel on 2/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        String log4jConfPath = "C:\\Users\\Michiel\\SoftwareEngineering3\\src\\main\\log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        TimePeriod p1 = new TimePeriod(15, 18, 2000);
        TimePeriod p2 = new TimePeriod(18, 19, 5000);
        ArrayList<TimePeriod> periods = new ArrayList<>();
        periods.add(p2);
        periods.add(p1);
        FrequencySchedule f = new FrequencySchedule(periods);
        DayScheduler dayScheduler = new DayScheduler(f);
        BagageScheduler bagageScheduler = new BagageScheduler(f.getCurrentTimePeriod());
        dayScheduler.addObserver(bagageScheduler);
        Thread day = new Thread(dayScheduler);
        Thread bagage = new Thread(bagageScheduler);
        day.start();
        bagage.start();
        List<TimePeriod> freq = f.getSchedule();
        for (TimePeriod p : freq) {
            System.out.println(p.getBeginHour());
        }
    }
}
