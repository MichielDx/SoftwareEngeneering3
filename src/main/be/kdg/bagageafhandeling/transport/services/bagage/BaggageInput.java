package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.adapters.in.RecordReader;
import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;
import main.be.kdg.bagageafhandeling.transport.exceptions.RecordReaderException;
import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageRecordDTO;
import main.be.kdg.bagageafhandeling.transport.models.baggage.BaggageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.BaggageConversionService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 3/11/2016.
 */
public class BaggageInput {
    private RecordReader recordReader;
    private BaggageConversionService recordDeserializer;
    private BaggageRecordList baggageRecordList;
    private String result;
    private Logger logger = Logger.getLogger(BaggageInput.class);

    public BaggageInput(String path, BaggageConversionService baggageConversionService) {
        recordReader = new RecordReader(path);
        recordDeserializer = baggageConversionService;
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
        baggageRecordList = recordDeserializer.deserializeAll(result);
    }

    public BaggageRecordDTO getNextBagage() throws EndReplayException{
        Baggage baggage = baggageRecordList.get(0);
        Baggage nextBaggage = baggageRecordList.get(1);
        long frequency;
        if(nextBaggage !=null)
        frequency = nextBaggage.getTimestamp().getTime()- baggage.getTimestamp().getTime();
        else{
            frequency=2000;
        }
        baggageRecordList.pop();
        return new BaggageRecordDTO(baggage,frequency);
    }


}
