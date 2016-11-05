package main.be.kdg.bagageafhandeling.transport.services.route;

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
public class RouteXmlService {
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;
    private StringReader reader;
    private StringWriter sw;
    private String xmlString;

    public String serialize(SensorMessage sensorMessage) {
        try {
            jaxbContext = JAXBContext.newInstance(SensorMessage.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(sensorMessage, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
