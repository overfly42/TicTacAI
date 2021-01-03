package MyTicTacAI2.Player;

import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;

public class NonePlayer extends Player {

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Message msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Message msg, Map<Keys, String> content) {
        // TODO Auto-generated method stub

    }

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void start(String queueName) {

    }

    @Override
    public boolean hasTurn() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clickOn(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void stop() {
    }

    @Override
    protected void sendBasicMessage(Message msg) {
    }

    @Override
    protected void registratonOpen() {
    }

    @Override
    public String toString() {
        return "Extern";
    }
}
