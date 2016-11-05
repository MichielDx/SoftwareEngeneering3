package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.transport.adapters.out.RecordWriter;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.exceptions.RecordWriterException;
import main.be.kdg.bagageafhandeling.transport.models.bagage.Bagage;
import main.be.kdg.bagageafhandeling.transport.models.bagage.BagageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.BagageConversionService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BagageOutput {
    private RabbitMQ rabbitMQ;
    private boolean record = false;
    private RecordWriter recorder;
    private BagageXmlService rabbitSerializer;
    private BagageConversionService recordSerializer;
    private BagageRecordList bagageRecordList;
    private BagageRepository bagageRepository;
    private Logger logger = Logger.getLogger(BagageOutput.class);

    public BagageOutput() {
        rabbitMQ = new RabbitMQ("bagageOutputQueue");
        initialize();
        rabbitSerializer = new BagageXmlService();
    }

    public BagageOutput(String recordPath, BagageConversionService recordSerializer, boolean record) {
        rabbitMQ = new RabbitMQ("bagageOutputQueue");
        recorder = new RecordWriter(recordPath);
        bagageRepository = new BagageRepository();
        bagageRecordList = new BagageRecordList();
        this.record = record;
        initialize();
        rabbitSerializer = new BagageXmlService();
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

    public void publish(Bagage bagage) {
        try {
            rabbitMQ.publish(rabbitSerializer.serialize(bagage));
            bagageRepository.addBagage(bagage);
            if (this.record) {
                bagageRecordList.add(bagage);
            }
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void write() {
        try {
            recorder.write(recordSerializer.serializeAll(bagageRecordList));
        } catch (RecordWriterException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }
}
