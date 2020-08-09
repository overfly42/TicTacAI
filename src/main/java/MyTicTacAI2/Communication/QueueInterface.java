package MyTicTacAI2.Communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private String tx;
    private String rx;
    private Set<IChangeListener> listener;

    private Translator msgTranslator;

    public QueueInterface() {
        this(true);
    }

    public QueueInterface(boolean actAsServer) {
        tx = actAsServer ? SEND_QUEUE_NAME : RECEIVE_QUEUE_NAME;
        rx = actAsServer ? RECEIVE_QUEUE_NAME : SEND_QUEUE_NAME;
        listener = new HashSet<>();
        msgTranslator = new Translator();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("New Message arrived: " + message);
            (new Thread() {
                public void run() {
                    processMassage(message);
                }
            }).start();
        };

        try {
            Connection con = factory.newConnection();
            rxChannel = con.createChannel();
            txChannel = con.createChannel();
            rxChannel.queueDeclare(rx, false, false, false, null);
            txChannel.queueDeclare(tx, false, false, false, null);
            rxChannel.basicConsume(rx, true, deliverCallback, consumerTag -> {
                System.out.println("echo");
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
            txChannel.basicPublish("", tx, null, msgTranslator.toQueue(msg, content).getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private synchronized void processMassage(String msg) {
        Map<Keys, String> content = new HashMap<>();
        Message message = msgTranslator.fromQueue(msg, content);
        for (IChangeListener l : listener) {
            (new Thread(new Runnable() {
                public void run() {
                    l.update(message, content);
                }
            })).start();
        }
    }

}