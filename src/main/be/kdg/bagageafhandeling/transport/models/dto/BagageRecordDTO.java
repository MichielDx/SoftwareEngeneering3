package main.be.kdg.bagageafhandeling.transport.models.dto;

import main.be.kdg.bagageafhandeling.transport.models.bagage.Bagage;

/**
 * Created by Michiel on 3/11/2016.
 */
public class BagageRecordDTO {
    private Bagage bagage;
    private long frequency;

    public BagageRecordDTO(Bagage bagage, long frequency) {
        this.bagage = bagage;
        this.frequency = frequency;
    }

    public Bagage getBagage() {
        return bagage;
    }

    public long getFrequency() {
        return frequency;
    }
}
