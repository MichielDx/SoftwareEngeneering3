package main.be.kdg.bagageafhandeling.transport.model.DTO;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Michiel on 4/11/2016.
 */
@XmlRootElement(name="sensor")
public class SensorMessageDTO {
    @XmlElement
    private int bagageId;
    @XmlElement
    private int conveyorId;
    @XmlElement
    private Date timestamp;

    public SensorMessageDTO() {
    }

    public SensorMessageDTO(int bagageId, int conveyorId, Date timestamp) {
        this.bagageId = bagageId;
        this.conveyorId = conveyorId;
        this.timestamp = timestamp;
    }

    public int getBagageId() {
        return bagageId;
    }

    public int getConveyorId() {
        return conveyorId;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
