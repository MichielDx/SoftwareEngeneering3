package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.transport.adapters.out.RecordWriter;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.exceptions.RecordWriterException;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.baggage.BaggageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.BaggageConversionService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BaggageOutput {
    private RabbitMQ rabbitMQ;
    private boolean record = false;
    private RecordWriter recorder;
    private BaggageXmlService rabbitSerializer;
    private BaggageConversionService recordSerializer;
    private BaggageRecordList baggageRecordList;
    private BaggageRepository baggageRepository;
    private Logger logger = Logger.getLogger(BaggageOutput.class);

    public BaggageOutput() {
        rabbitMQ = new RabbitMQ("bagageOutputQueue");
        initialize();
        rabbitSerializer = new BaggageXmlService();
    }

    public BaggageOutput(String recordPath, BaggageConversionService recordSerializer, boolean record) {
        rabbitMQ = new RabbitMQ("bagageOutputQueue");
        recorder = new RecordWriter(recordPath);
        baggageRepository = new BaggageRepository();
        baggageRecordList = new BaggageRecordList();
        this.record = record;
        initialize();
        rabbitSerializer = new BaggageXmlService();
        this.recordSerializer = recordSerializer;

    }

    private void initialize() {
        try {
            rabbitMQ.initialize();
            if (this.record) {
                recorder.initialize();
            }
        } catch (MessageOutputException | RecordWriterException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void publish(Baggage baggage) {
        try {
            rabbitMQ.publish(rabbitSerializer.serialize(baggage));
            baggageRepository.addBagage(baggage);
            if (this.record) {
                baggageRecordList.add(baggage);
            }
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
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
