package main.be.kdg.bagageafhandeling.transport.services.interfaces;

import main.be.kdg.bagageafhandeling.transport.models.baggage.BaggageRecordList;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public interface BaggageConversionService {
    String serializeAll(BaggageRecordList bagages);
    BaggageRecordList deserializeAll(String string);
}
