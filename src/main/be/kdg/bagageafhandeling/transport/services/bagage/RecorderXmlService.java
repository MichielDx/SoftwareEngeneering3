package main.be.kdg.bagageafhandeling.transport.services.bagage;

import main.be.kdg.bagageafhandeling.transport.models.baggage.Baggage;
import main.be.kdg.bagageafhandeling.transport.models.baggage.BaggageRecordList;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.RecorderConversionService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Michiel on 2/11/2016.
 */
public class RecorderXmlService implements RecorderConversionService {
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;
    private StringReader reader;
    private StringWriter sw;
    private String xmlString;


    @Override
    public String serializeAll(BaggageRecordList bagages) {
        try {
            jaxbContext = JAXBContext.newInstance(BaggageRecordList.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(bagages, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    @Override
    public BaggageRecordList deserializeAll(String string) {
        BaggageRecordList baggageRecordList = null;
        try {
            jaxbContext = JAXBContext.newInstance(BaggageRecordList.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            sw = new StringWriter();
            reader = new StringReader(string);
            baggageRecordList = (BaggageRecordList) jaxbUnmarshaller.unmarshal(reader);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return baggageRecordList;
    }
}
