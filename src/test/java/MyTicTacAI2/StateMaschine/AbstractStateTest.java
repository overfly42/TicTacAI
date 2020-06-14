package MyTicTacAI2.StateMaschine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IComQueue;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;

public abstract class AbstractStateTest<STATE extends IGameState> implements IComQueue, IGameStateMachine {
    protected GameState lastStateSwitchedTo = null;
    protected boolean stateSwitchCalled = false;
    protected GameBoard board;
    protected IChangeListener comPartner;
    protected List<Message> lastMessages = null;
    protected List<Map<Keys, String>> lastMessagesContent = null;
    protected Message lastMessage = null;
    protected Map<Keys, String> lastMessageContent = null;

    STATE testObject;
    
    @BeforeEach
    public void preperation() {
        board = new GameBoard();
        lastStateSwitchedTo = null;
        lastMessage = null;
        lastMessages = new ArrayList<>();
        lastMessageContent = null;
        lastMessagesContent = new ArrayList<>();
        stateSwitchCalled = false;
    }

    @Override
    public void setToState(GameState next) {
        lastStateSwitchedTo = next;
        stateSwitchCalled = true;
    }

    @Override
    public GameBoard getBoard() {
        return board;
    }

    @Override
    public void addListener(IChangeListener listner) {
        comPartner = listner;
    }

    @Override
    public void removeListener(IChangeListener listener) {
        if (comPartner == listener)
            listener = null;
    }

    @Override
    public void sendMessage(Message msg) {
        sendMessage(msg, null);
    }

    @Override
    public void sendMessage(Message msg, Map<Keys, String> content) {
        lastMessage = msg;
        lastMessageContent = content;
        lastMessages.add(msg);
        lastMessagesContent.add(content);
    }

    @Override
    public GameState getCurrentState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isActivated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void startStateMaschine() {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopStateMaschine() {
        // TODO Auto-generated method stub

    }

}