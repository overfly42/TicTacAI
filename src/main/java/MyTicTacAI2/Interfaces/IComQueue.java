package MyTicTacAI2.Interfaces;

import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;

public interface IComQueue {
  void addListener(IChangeListener listener);

  void removeListener(IChangeListener listener);

  void sendMessage(Message msg);

  void sendMessage(Message msg, Map<Keys, String> content);

}