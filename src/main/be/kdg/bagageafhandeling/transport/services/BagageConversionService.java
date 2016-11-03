package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.model.Bagage;

/**
 * Created by Arthur Haelterman on 3/11/2016.
 */
public interface BagageConversionService {
    String serialize(Bagage bagage);
}
