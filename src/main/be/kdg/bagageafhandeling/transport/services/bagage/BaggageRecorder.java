package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.adapters.out.RecordWriter;
import main.be.kdg.bagageafhandeling.transport.exceptions.RecordWriterException;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.baggage.BaggageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.RecorderConversionService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BaggageRecorder {
    private RecordWriter recorder;
    private RecorderConversionService recordSerializer;
    private BaggageRecordList baggageRecordList;
    private BaggageRepository baggageRepository;
    private Logger logger = Logger.getLogger(BaggageRecorder.class);

    public BaggageRecorder(String recordPath, RecorderConversionService recordSerializer) {
        recorder = new RecordWriter(recordPath);
        baggageRepository = new BaggageRepository();
        baggageRecordList = new BaggageRecordList();
        initialize();
        this.recordSerializer = recordSerializer;

    }

    private void initialize() {
        try {
                recorder.initialize();
        } catch (RecordWriterException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void record(Baggage baggage) {
                baggageRecordList.add(baggage);
    }

    public void write() {
        try {
            recorder.write(recordSerializer.serializeAll(baggageRecordList));
        } catch (RecordWriterException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }
}
