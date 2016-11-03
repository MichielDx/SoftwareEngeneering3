package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.model.Bagage;
import main.be.kdg.bagageafhandeling.transport.model.BagageRecordList;

import java.util.List;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public interface BagageConversionService {
    String serializeAll(BagageRecordList bagages);
    BagageRecordList deserializeAll(String string);
}
