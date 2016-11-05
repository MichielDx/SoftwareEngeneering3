package main.be.kdg.bagageafhandeling.transport.models.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michiel on 3/11/2016.
 */
@XmlRootElement(name="bagage")
public class BagageMessageDTO {
    @XmlElement
    private int bagageID;
    @XmlElement
    private int conveyorID;

    public BagageMessageDTO() {
        
    }

    public BagageMessageDTO(int bagageID, int conveyorID) {
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

