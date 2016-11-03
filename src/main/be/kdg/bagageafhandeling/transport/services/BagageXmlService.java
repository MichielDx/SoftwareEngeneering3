package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.model.Bagage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BagageXmlService implements BagageConversionService {
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private StringWriter sw;
    private String xmlString;

    public String serialize(Bagage bagage) {
        try {
            jaxbContext = JAXBContext.newInstance(Bagage.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(bagage, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
