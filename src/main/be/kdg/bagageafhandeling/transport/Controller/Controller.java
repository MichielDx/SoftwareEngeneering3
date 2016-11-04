package main.be.kdg.bagageafhandeling.transport.Controller;

import main.be.kdg.bagageafhandeling.transport.engine.BagageScheduler;
import main.be.kdg.bagageafhandeling.transport.engine.DayScheduler;
import main.be.kdg.bagageafhandeling.transport.engine.RouteScheduler;
import main.be.kdg.bagageafhandeling.transport.model.Enum.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.model.Enum.FormatOption;
import main.be.kdg.bagageafhandeling.transport.model.Enum.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.model.FrequencySchedule;
import main.be.kdg.bagageafhandeling.transport.model.TimePeriod;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arthur Haelterman on 4/11/2016.
 */
public class Controller {

    private String log4jConfPath = "C:\\Users\\Michiel\\SoftwareEngineering3\\src\\main\\log4j.properties";
    private FrequencySchedule f;
    private DayScheduler dayScheduler;
    private BagageScheduler bagageScheduler;
    private RouteScheduler routeScheduler;
    private Thread day;
    private Thread bagage;
    private FormatOption option;
    private SimulatorMode mode;
    private DelayMethod method;
    private String recordPath;


    public Controller(){
        f = getFrequencySchedule();
        PropertyConfigurator.configure(log4jConfPath);
        this.dayScheduler = new DayScheduler(f);
    }

    public void initialize(){
        bagageScheduler = new BagageScheduler(f.getCurrentTimePeriod(),recordPath,option,mode);
        routeScheduler = new RouteScheduler(method,2000,getSecurityList());
        dayScheduler = new DayScheduler(f);
        day = new Thread(dayScheduler);
        bagage = new Thread(bagageScheduler);
    }


    public void start(){
        day.start();
        bagage.start();
    }


    public void setOption(FormatOption option) {
        this.option = option;
    }

    public void setMode(SimulatorMode mode) {
        this.mode = mode;
    }

    public void setMethod(DelayMethod method) {
        this.method = method;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    private Map<Integer,Integer> getSecurityList(){
        Map<Integer,Integer> hashMap = new HashMap<>();
        return hashMap;
    }


    private FrequencySchedule getFrequencySchedule(){
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
