package MyTicTacAI2.Communication;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rabbitmq.client.*;

import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IComQueue;

public abstract class BaseQueue implements IComQueue {

    public static final String EXCHANGE_NAME = "message_from_server";
    public static final String BORADCAST_KEY = "all";
    protected final String receivingQueue;
    protected final String sendingExchangeName;
    protected Channel txChannel;
    protected Channel rxChannel;

    private Set<IChangeListener> listener;

    public BaseQueue(String QueueName, String sendingExchangeName) {
        receivingQueue = QueueName;
        this.sendingExchangeName = sendingExchangeName;
        listener = Collections.synchronizedSet(new HashSet<>());
        setupChannel();
        try {
            rxChannel.basicConsume(QueueName, true, deliverCallback, consumerTag -> {
            });

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    protected abstract void setupChannel();

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println("New Message arrived in ("+delivery.getEnvelope().getExchange()+"):"+ message);
        (new Thread() {
            public void run() {
                processMassage(message);
            }
        }).start();
    };

    protected synchronized void processMassage(String msg) {
        Map<Keys, String> content = new HashMap<>();
        Message message = Translator.fromQueue(msg, content);
        synchronized (listener) {
            for (IChangeListener l : listener) {
                (new Thread(new Runnable() {
                    public void run() {
                        l.update(message, content);
                    }
                })).start();
            }
        }
    }

    @Override
    public void addListener(IChangeListener listener) {
        System.out.println("Added Listener for: " + listener.toString());
        synchronized (listener) {
            this.listener.add(listener);
        }
    }

    @Override
    public void removeListener(IChangeListener listener) {
        synchronized (listener) {
            this.listener.remove(listener);
        }
    }

    @Override
    public void sendMessage(Message msg) {
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, "all");
        sendMessage(msg, content);
    }

    @Override
    public void sendMessage(Message msg, Map<Keys, String> content) {
        try {
            txChannel.basicPublish(sendingExchangeName, content.get(Keys.ID), null,
                    Translator.toQueue(msg, content).getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}