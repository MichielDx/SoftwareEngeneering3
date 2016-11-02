package main.be.kdg.bagageafhandeling.transport.model;

import main.be.kdg.bagageafhandeling.transport.exceptions.TimePeriodException;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Michiel on 2/11/2016.
 */
public class FrequencySchedule {
    private List<TimePeriod> schedule = new ArrayList<>();
    private Logger logger = Logger.getLogger(FrequencySchedule.class);
    private Date date;
    private int hour;

    public FrequencySchedule(List<TimePeriod> periods) {
        for (TimePeriod period : periods) {
            try {
                period.validatePeriod();
                addPeriod(period);
            } catch (TimePeriodException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void addPeriod(TimePeriod period) {
        boolean check = true;
        for (TimePeriod time : schedule) {
            try {
                check = time.checkTimeConflict(period);
            } catch (TimePeriodException e) {
                logger.error(e.getMessage());
            }
        }
        if (schedule.size() == 0) {
            schedule.add(period);
        }
        if (!check) {
            schedule.add(period);
            Collections.sort(schedule);
        }
    }


    public List<TimePeriod> getSchedule() {
        return schedule;
    }

    public TimePeriod getCurrentTimePeriod() {
        int hour = LocalDateTime.now().getHour();
        TimePeriod timePeriod = null;
        for (TimePeriod period : schedule) {
            if (hour >= period.getBeginHour()) {
                timePeriod = period;
            }
        }
        return timePeriod;
    }
}
