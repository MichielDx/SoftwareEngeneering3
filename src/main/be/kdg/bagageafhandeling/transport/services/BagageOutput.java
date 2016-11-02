package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.model.Bagage;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 2/11/2016.
 */
public class BagageOutput {
    private RabbitMQ rabbitMQ;
    private BagageXmlService bagageXmlService;
    private Logger logger = Logger.getLogger(BagageOutput.class);

    public BagageOutput() {
        rabbitMQ = new RabbitMQ("bagageQueue");
        initialize();
        bagageXmlService = new BagageXmlService();
    }

    private void initialize(){
        try {
            rabbitMQ.initialize();
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
        }
    }
    
    public void publishMessage(Bagage bagage){
        rabbitMQ.publish(bagageXmlService.getXml(bagage));
    }
}
