package main.be.kdg.bagageafhandeling.transport.models.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michiel on 3/11/2016.
 */
@XmlRootElement(name="baggage")
public class BaggageMessageDTO {
    @XmlElement
    private int bagageID;
    @XmlElement
    private int conveyorID;

    public BaggageMessageDTO() {
        
    }

    public BaggageMessageDTO(int bagageID, int conveyorID) {
        this.bagageID = bagageID;
        this.conveyorID = conveyorID;
    }

    public int getBagageID() {
        return bagageID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    @Override
    public String toString() {
        return "BagageId: " + bagageID + " | " + "ConveyorID " + conveyorID;
    }
}

