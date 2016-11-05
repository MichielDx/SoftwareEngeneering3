package main.be.kdg.bagageafhandeling.transport.models.baggage;

import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michiel on 3/11/2016.
 */
@XmlRootElement(name="baggages")
public class BaggageRecordList {
    @XmlElement(name = "baggage")
    private List<Baggage> baggages;

    public BaggageRecordList() {
        this.baggages = new LinkedList<>();
    }

    public List<Baggage> getBaggages() {
        return baggages;
    }

    public void add(Baggage baggage){
        baggages.add(baggage);
    }

    public Baggage get(int index){
        if(index>= baggages.size()){
            return null;
        }
        return baggages.get(index);
    }

    public void pop() throws EndReplayException{
        try {
            baggages.remove(0);
        } catch (IndexOutOfBoundsException e){
            throw new EndReplayException("All baggages have been read");
        }
    }
}
