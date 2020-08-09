package MyTicTacAI2.UI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Communication.QueueInterface;
import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IComQueue;
import javafx.beans.property.SimpleStringProperty;

public class HumanPlayer implements IChangeListener {

    IComQueue com;
    List<SimpleStringProperty> field;
    SimpleStringProperty playerA;
    SimpleStringProperty playerB;

    public HumanPlayer(List<SimpleStringProperty> fieldView, SimpleStringProperty playerAProperty,
            SimpleStringProperty playerBProperty) {
        com = new QueueInterface(false);
        com.addListener(this);
        field = fieldView;
        playerA = playerAProperty;
        playerB = playerBProperty;
    }

    public void stop() {
    }

    public void start() {
        for (SimpleStringProperty simpleStringProperty : field) {
            simpleStringProperty.set("x");
        }
    }

    @Override
    public void update() {
        System.out.println("Update called");
    }

    @Override
    public void update(Message msg) {
        System.out.println("Update: " + msg);
    }

    @Override
    public void update(Message msg, Map<Keys, String> content) {
        System.out.println("Update: " + msg);
        for (Keys k : content.keySet())
            System.out.println("\t" + k.toString() + "\t= " + content.get(k));
        switch (msg) {
            case EndGame:
                break;
            case EndSession:
                break;
            case PlayerReady:
                break;
            case Register:
                break;
            case RegisterOpen:
                registratonOpen();
                break;
            case RegisterRejected:
                break;
            case RegisterSuccess:
                break;
            case Set:
                break;
            case SetRejected:
                break;
            case StartGame:
                break;
            case StartSession:
                break;
            case Turn:
                break;
            default:
                break;

        }
    }

    private void registratonOpen() {
        Map<Keys,String> content = new HashMap<>();
        Message msg = Message.Register;
        content.put(Keys.ID, playerA.get());
        com.sendMessage(msg, content);
        content.put(Keys.ID, playerB.get());
        com.sendMessage(msg,content);
    }

}
