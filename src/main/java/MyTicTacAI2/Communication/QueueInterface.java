package MyTicTacAI2.Communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IComQueue;

public class QueueInterface implements IComQueue {
    private final static String RECEIVE_QUEUE_NAME = "toServer";
    private final static String SEND_QUEUE_NAME = "fromServer";
    private Channel rxChannel;
    private Channel txChannel;
    private List<IChangeListener> listener;

    private Translator msgTranslator;

    public QueueInterface() {
        listener = new ArrayList<>();
        msgTranslator = new Translator();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            processMassage(message);
        };

        try {
            Connection con = factory.newConnection();
            rxChannel = con.createChannel();
            txChannel = con.createChannel();
            rxChannel.queueDeclare(RECEIVE_QUEUE_NAME, false, false, false, null);
            txChannel.queueDeclare(SEND_QUEUE_NAME, false, false, false, null);
            rxChannel.basicConsume(RECEIVE_QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Queues created");
    }

    @Override
    public void addListener(IChangeListener listener) {
        this.listener.add(listener);

    }

    @Override
    public void removeListener(IChangeListener listener) {
        this.listener.remove(listener);

    }

    @Override
    public void sendMessage(Message msg) {
        sendMessage(msg, new HashMap<>());
    }

    @Override
    public void sendMessage(Message msg, Map<Keys, String> content) {
        try {
            txChannel.basicPublish("", RECEIVE_QUEUE_NAME, null, msgTranslator.toQueue(msg, content).getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processMassage(String msg) {
        Map<Keys, String> content = new HashMap<>();
        Message message = msgTranslator.fromQueue(msg, content);
        for (IChangeListener l : listener) {
            l.update(message, content);
        }
    }

}