package main.be.kdg.bagageafhandeling.transport.adapters.in;

import com.rabbitmq.client.*;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.models.dto.BaggageMessageDTO;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageInputService;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;

/**
 * Created by Michiel on 2/11/2016.
 */
public class RabbitMQIn extends Observable implements MessageInputService {
    private final String queueName;
    JAXBContext jaxbContext;
    Unmarshaller jaxbUnmarshaller;
    private Connection connection;
    private Channel channel;

    private Logger logger = Logger.getLogger(RabbitMQIn.class);

    public RabbitMQIn(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void initialize() throws MessageInputException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);

        } catch (IOException | TimeoutException e) {
            throw new MessageInputException("Unable to connect to RabbitMQIn", e);
        }
        logger.info("Succesfully connected to RabbitMQIn queue: " + queueName);
    }

    @Override
    public void shutdown() throws MessageInputException {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new MessageInputException("Unable to close connection to RabbitMQIn", e);
        }
    }


    public void addObserver(Observer o){
        super.addObserver(o);
    }

    public void retrieve() throws MessageInputException {
        try {
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    try {
                        jaxbContext = JAXBContext.newInstance(BaggageMessageDTO.class);
                        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        Reader reader = new StringReader(message);
                        BaggageMessageDTO messageDTO = null;
                        messageDTO = (BaggageMessageDTO) jaxbUnmarshaller.unmarshal(reader);
                        setChanged();
                        notifyObservers(messageDTO);
                    } catch (Exception e) {
                        throw new IOException("Error during conversion from RabbitMQIn message to BaggageMessageDTO", e);
                    }
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            throw new MessageInputException("Unable to retrieve message from RabbitMQIn", e);
        }

    }

}


