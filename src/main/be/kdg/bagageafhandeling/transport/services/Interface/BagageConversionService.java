package main.be.kdg.bagageafhandeling.transport.services.Interface;

import main.be.kdg.bagageafhandeling.transport.model.Bagage.BagageRecordList;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public interface BagageConversionService {
    String serializeAll(BagageRecordList bagages);
    BagageRecordList deserializeAll(String string);
}
