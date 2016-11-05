package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.adapters.in.RecordReader;
import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;
import main.be.kdg.bagageafhandeling.transport.exceptions.RecordReaderException;
import main.be.kdg.bagageafhandeling.transport.models.bagage.Bagage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BagageRecordDTO;
import main.be.kdg.bagageafhandeling.transport.models.bagage.BagageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.BagageConversionService;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageInputService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 3/11/2016.
 */
public class BagageInput {
    private RecordReader recordReader;
    private BagageConversionService recordDeserializer;
    private BagageRecordList bagageRecordList;
    private String result;
    private Logger logger = Logger.getLogger(BagageInput.class);
    private MessageInputService rabbitMQ;

    public BagageInput(String path, BagageConversionService bagageConversionService) {
        recordReader = new RecordReader(path);
        recordDeserializer = bagageConversionService;
        initialize();
    }

    public void initialize() {
        try {
            result = recordReader.initialize();
            getBagageRecordList();
        } catch (RecordReaderException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void getBagageRecordList() {
        bagageRecordList = recordDeserializer.deserializeAll(result);
    }

    public BagageRecordDTO getNextBagage() throws EndReplayException{
        Bagage bagage = bagageRecordList.get(0);
        Bagage nextBagage = bagageRecordList.get(1);
        long frequency;
        if(nextBagage!=null)
        frequency = nextBagage.getTimestamp().getTime()-bagage.getTimestamp().getTime();
        else{
            frequency=2000;
        }
        bagageRecordList.pop();
        return new BagageRecordDTO(bagage,frequency);
    }


}
