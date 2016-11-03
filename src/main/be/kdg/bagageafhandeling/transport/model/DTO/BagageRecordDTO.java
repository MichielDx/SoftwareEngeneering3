package main.be.kdg.bagageafhandeling.transport.model.DTO;

import main.be.kdg.bagageafhandeling.transport.model.Bagage;

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
