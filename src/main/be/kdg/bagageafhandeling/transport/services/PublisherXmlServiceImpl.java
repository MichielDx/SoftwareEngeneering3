package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.models.SensorMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Michiel on 4/11/2016.
 */
public class PublisherXmlServiceImpl {
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private StringWriter sw;
    private String xmlString;

    public String serialize(Object object) {
        try {
            jaxbContext = JAXBContext.newInstance(object.getClass());
            jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(object, sw);
            xmlString = sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
