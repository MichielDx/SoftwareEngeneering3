package main.be.kdg.bagageafhandeling.transport.adapters.out;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageOutputService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Michiel on 2/11/2016.
 */
public class RabbitMQOut implements MessageOutputService {
    private final String queueName;

    private Connection connection;
    private Channel channel;

    private Logger logger = Logger.getLogger(RabbitMQOut.class);

    public RabbitMQOut(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void initialize() throws MessageOutputException{
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);

        } catch (IOException | TimeoutException e) {
            throw new MessageOutputException("Unable to connect to RabbitMQOut",e);
        }
        logger.info("Succesfully connected to RabbitMQOut queue: " + queueName);
    }

    @Override
    public void shutdown() throws MessageOutputException{
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new MessageOutputException("Unable to close connection to RabbitMQOut",e);
        }
    }


    public void publish(String message) throws MessageOutputException {
        try {
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            throw new MessageOutputException("Unable to publish message to RabbitMQOut queue: " + queueName,e);
        }
    }
}
