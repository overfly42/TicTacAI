package MyTicTacAI2.Interfaces;

import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;

public interface IChangeListener {
    public void update();
    public void update(Message msg);
    public void update(Message msg,Map<Keys,String> content);
}