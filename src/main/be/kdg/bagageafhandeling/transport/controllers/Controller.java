package main.be.kdg.bagageafhandeling.transport.controllers;

import main.be.kdg.bagageafhandeling.transport.engines.BaggageScheduler;
import main.be.kdg.bagageafhandeling.transport.engines.DayScheduler;
import main.be.kdg.bagageafhandeling.transport.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.transport.models.enums.DelayMethod;
import main.be.kdg.bagageafhandeling.transport.models.enums.FormatOption;
import main.be.kdg.bagageafhandeling.transport.models.enums.SimulatorMode;
import main.be.kdg.bagageafhandeling.transport.models.FrequencySchedule;
import main.be.kdg.bagageafhandeling.transport.models.TimePeriod;
import main.be.kdg.bagageafhandeling.transport.services.Publisher;
import main.be.kdg.bagageafhandeling.transport.services.PublisherXmlServiceImpl;
import main.be.kdg.bagageafhandeling.transport.services.Retriever;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageInputService;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageOutputService;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arthur Haelterman on 4/11/2016.
 */
public class Controller {
    private String path = new File("src/main/log4j.properties").getAbsolutePath();
    //private String log4jConfPath = "C:\\Users\\Arthur Haelterman\\SoftwareEngineering3\\src\\main\\log4j.properties";
    private FrequencySchedule f;
    private DayScheduler dayScheduler;
    private BaggageScheduler baggageScheduler;
    private RouteScheduler routeScheduler;
    private Thread day;
    private Thread bagage;
    private FormatOption option;
    private SimulatorMode mode;
    private DelayMethod method;
    private String recordPath;
    private ConveyorService conveyorService;
    private MessageInputService routeInputQueue;
    private MessageOutputService sensorOutputQueue;

    public Controller(){
    }

    public void initialize(){
        f = getFrequencySchedule();
        PropertyConfigurator.configure(path);
        PublisherXmlServiceImpl publisherXmlService = new PublisherXmlServiceImpl();
        Publisher sensorMessagePublisher = new Publisher(sensorOutputQueue, publisherXmlService);
        this.dayScheduler = new DayScheduler(f);
        baggageScheduler = new BaggageScheduler(f.getCurrentTimePeriod(),recordPath,option,mode);
        routeScheduler = new RouteScheduler(conveyorService,method,2000,getSecurityList(),sensorMessagePublisher);
        Retriever routeInputRetriever = new Retriever(routeInputQueue ,routeScheduler);
        dayScheduler = new DayScheduler(f);
        day = new Thread(dayScheduler);
        bagage = new Thread(baggageScheduler);
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

    public void setConveyorService(ConveyorService conveyorService) {
        this.conveyorService = conveyorService;
    }

    public void setRouteInputQueue(MessageInputService routeInputQueue) {
        this.routeInputQueue = routeInputQueue;
    }

    public void setSensorOutputQueue(MessageOutputService sensorOutputQueue) {
        this.sensorOutputQueue = sensorOutputQueue;
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
