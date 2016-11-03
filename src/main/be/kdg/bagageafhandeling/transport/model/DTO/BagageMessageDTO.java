package main.be.kdg.bagageafhandeling.transport.model.DTO;

/**
 * Created by Michiel on 3/11/2016.
 */
public class BagageMessageDTO {
    private int BagageID;
    private int ConveyorID;

    public BagageMessageDTO(int bagageID, int conveyorID) {
        BagageID = bagageID;
        ConveyorID = conveyorID;
    }

    public int getBagageID() {
        return BagageID;
    }

    public int getConveyorID() {
        return ConveyorID;
    }

    @Override
    public String toString() {
        return "BagageId: " + BagageID + " | " + "ConveyorID " + ConveyorID;
    }
}

