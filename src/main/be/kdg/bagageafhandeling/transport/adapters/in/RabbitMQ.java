package main.be.kdg.bagageafhandeling.transport.adapters.in;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.transport.services.MessageInputService;
import main.be.kdg.bagageafhandeling.transport.services.MessageOutputService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;

/**
 * Created by Michiel on 2/11/2016.
 */
public class RabbitMQ implements MessageInputService {
    private final String queueName;

    private Connection connection;
    private Channel channel;

    private Logger logger = Logger.getLogger(RabbitMQ.class);

    public RabbitMQ(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void initialize() throws MessageInputException{
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);

        } catch (IOException | TimeoutException e) {
            throw new MessageInputException("Unable to connect to RabbitMQ",e);
        }
    }

    @Override
    public void shutdown() throws MessageInputException{
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new MessageInputException("Unable to close connection to RabbitMQ",e);
        }
    }


    public void publish(String message) throws MessageInputException {
        try {
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            throw new MessageInputException("Unable to publish message to RabbitMQ",e);
        }
    }
}
