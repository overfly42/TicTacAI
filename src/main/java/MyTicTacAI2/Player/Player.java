package MyTicTacAI2.Player;

import java.util.HashMap;
import java.util.Map;

import MyTicTacAI2.Communication.ClientQueue;
import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IComQueue;

public abstract class Player implements IChangeListener {

    protected boolean isActive;
    protected IComQueue com;
    private String lastQueue = "";

    public abstract void start();

    protected void start(String queueName) {
        isActive = true;
        if (com != null && !lastQueue.equals(queueName))
            com.addListener(this);
        else {
            if (queueName != null)
                lastQueue = queueName;
            com = new ClientQueue(queueName, "server_in");
            com.addListener(this);
        }
    }

    public void stop() {
        isActive = false;
    }

    public abstract boolean hasTurn();

    public abstract void clickOn(int x, int y);

    protected void sendBasicMessage(Message msg) {
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, getId());
        com.sendMessage(msg, content);

    }

    protected void registratonOpen() {
        sendBasicMessage(Message.Register);
    }
    protected abstract String getId();
}