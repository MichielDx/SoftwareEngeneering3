package main.be.kdg.bagageafhandeling.transport.services;

import main.be.kdg.bagageafhandeling.transport.adapters.in.RabbitMQ;
import main.be.kdg.bagageafhandeling.transport.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.transport.services.interfaces.MessageInputService;

import java.util.Observer;

/**
 * Created by Arthur Haelterman on 6/11/2016.
 */
public class Retriever {
    private MessageInputService rabbitMQ;
    public   Retriever(String queueName, Observer observer){
        rabbitMQ = new RabbitMQ(queueName);
        rabbitMQ.addObserver(observer);
    }
    public void initialize(){
        try {
            rabbitMQ.initialize();
            rabbitMQ.retrieve();
        } catch (MessageInputException e) {
            e.getMessage();
        }
    }
}
