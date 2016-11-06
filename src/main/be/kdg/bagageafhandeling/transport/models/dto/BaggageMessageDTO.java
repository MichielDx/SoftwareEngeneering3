package main.be.kdg.bagageafhandeling.transport.models.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michiel on 3/11/2016.
 */

@XmlRootElement(name="route")
public class BaggageMessageDTO {
    @XmlElement
    private int baggageID;
    @XmlElement
    private int conveyorID;

    public BaggageMessageDTO() {
        
    }

    public BaggageMessageDTO(int baggageID, int conveyorID) {
        this.baggageID = baggageID;
        this.conveyorID = conveyorID;
    }

    public int getBaggageID() {
        return baggageID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    @Override
    public String toString() {
        return "BagageId: " + baggageID + " | " + "ConveyorID " + conveyorID;
    }
}

