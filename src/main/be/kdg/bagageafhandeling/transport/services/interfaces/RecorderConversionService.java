package main.be.kdg.bagageafhandeling.transport.services.interfaces;

import main.be.kdg.bagageafhandeling.transport.models.baggage.BaggageRecordList;

/**
 * A RecorderConversionService serializes or deserializes all baggages in a BaggageRecordList to or from a certain format
 */
public interface RecorderConversionService {
    String serializeAll(BaggageRecordList baggages);

    BaggageRecordList deserializeAll(String string);
}
