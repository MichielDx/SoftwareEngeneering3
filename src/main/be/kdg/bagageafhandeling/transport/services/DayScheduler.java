package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.model.FrequencySchedule;
import main.be.kdg.bagageafhandeling.transport.model.TimePeriod;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Observable;

/**
 * Created by Michiel on 2/11/2016.
 */
public class DayScheduler extends Observable implements Runnable {
    private FrequencySchedule schedule;
    private Logger logger = Logger.getLogger(DayScheduler.class);

    public DayScheduler(FrequencySchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public void run() {
        TimePeriod timePeriod = schedule.getCurrentTimePeriod();
        while(true) {
            long sleep = (timePeriod.getEndHour() * 3600 * 1000) - getCurrentTime();
            try {
                Thread.sleep(sleep);
                timePeriod = schedule.getCurrentTimePeriod();
                logger.info("Changed timePeriod to "+ timePeriod);
                setChanged();
                notifyObservers(timePeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long getCurrentTime(){
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        return passed;
    }
}
