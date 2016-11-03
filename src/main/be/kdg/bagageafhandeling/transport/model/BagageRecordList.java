package main.be.kdg.bagageafhandeling.transport.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michiel on 3/11/2016.
 */
@XmlRootElement(name="bagages")
public class BagageRecordList {
    @XmlElement(name = "bagage")
    private List<Bagage> bagages;

    public BagageRecordList() {
        this.bagages = new ArrayList<>();
    }

    public List<Bagage> getBagages() {
        return bagages;
    }

    public void add(Bagage bagage){
        bagages.add(bagage);
    }
}
