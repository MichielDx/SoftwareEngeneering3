package main.be.kdg.bagageafhandeling.transport.models;

import main.be.kdg.bagageafhandeling.transport.exceptions.TimePeriodException;

/**
 * Created by Michiel on 2/11/2016.
 */
public class TimePeriod implements Comparable<TimePeriod> {
    private int beginHour;
    private int endHour;
    private long frequency;

    public TimePeriod(int beginHour, int endHour, long frequency) {
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.frequency = frequency;
    }

    public boolean checkTimeConflict(TimePeriod period) throws TimePeriodException {
        if((period.getBeginHour() >= this.beginHour && period.getBeginHour() < this.endHour) || (this.beginHour >= period.getBeginHour() && this.beginHour < period.getEndHour())){
            throw new TimePeriodException("schedule hours are not allowed to overlap");
        }
        return false;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public long getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(TimePeriod o) {
        return this.getBeginHour() - o.getBeginHour();
    }

    @Override
    public String toString() {
        return beginHour + "-" + endHour + " at a frequency of " + frequency;
    }
}
