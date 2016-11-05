package main.be.kdg.bagageafhandeling.transport.models.dto;

import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;

/**
 * Created by Michiel on 3/11/2016.
 */
public class BaggageRecordDTO {
    private Baggage baggage;
    private long frequency;

    public BaggageRecordDTO(Baggage baggage, long frequency) {
        this.baggage = baggage;
        this.frequency = frequency;
    }

    public Baggage getBaggage() {
        return baggage;
    }

    public long getFrequency() {
        return frequency;
    }
}
