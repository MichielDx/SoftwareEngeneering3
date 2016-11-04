package main.be.kdg.bagageafhandeling.transport.model.Bagage;

import main.be.kdg.bagageafhandeling.transport.exceptions.EndReplayException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michiel on 3/11/2016.
 */
@XmlRootElement(name="bagages")
public class BagageRecordList {
    @XmlElement(name = "bagage")
    private List<Bagage> bagages;

    public BagageRecordList() {
        this.bagages = new LinkedList<>();
    }

    public List<Bagage> getBagages() {
        return bagages;
    }

    public void add(Bagage bagage){
        bagages.add(bagage);
    }

    public Bagage get(int index){
        if(index>=bagages.size()){
            return null;
        }
        return bagages.get(index);
    }

    public void pop() throws EndReplayException{
        try {
            bagages.remove(0);
        } catch (IndexOutOfBoundsException e){
            throw new EndReplayException("All bagages have been read");
        }
    }
}
