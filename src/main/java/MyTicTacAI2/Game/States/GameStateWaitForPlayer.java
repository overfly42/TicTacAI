package MyTicTacAI2.Game.States;

import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IComQueue;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;

public class GameStateWaitForPlayer implements IGameState, IChangeListener {

    GameBoard board;
    IComQueue com;
    IGameStateMachine stateMaschine;
    private static final GameState NEXT_STATE = GameState.StartGame;

    public GameStateWaitForPlayer(IGameStateMachine gameStateMaschine, IComQueue com) {
        this.com = com;
        stateMaschine = gameStateMaschine;
        board = stateMaschine.getBoard();
    }

    @Override
    public void leave() {
        // TODO Auto-generated method stub
        this.com.removeListener(this);
    }

    @Override
    public void enter() {
        this.com.addListener(this);
        switch (board.getNumberOfPlayer()) {
            case 0:
                com.sendMessage(Message.RegisterOpen);
                break;
            case 1:
                break;
            case 2:
                switchToNextState();
                break;
            default:
                System.out.println("Error");
        }
    }

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
        if (msg != Message.Register || content == null || content.size() == 0)
            return;
        String id = content.get(Keys.ID);
        if ((board.getPlayerA() != null && board.getPlayerA().equals(id))
                || (board.getPlayerB() != null && board.getPlayerB().equals(id)) || board.getNumberOfPlayer() == 2)
            rejectPlayer(content);
        else
            try {
                String internalID = board.addPlayer(id);
                content.put(Keys.Player, internalID);
                acceptPlayer(content);
            } catch (Exception e) {
                rejectPlayer(content);
            }
        switchToNextState();
    }

    private void acceptPlayer(Map<Keys, String> content) {
        com.sendMessage(Message.RegisterSuccess,content);
    }

    private void rejectPlayer(Map<Keys, String> content) {
        com.sendMessage(Message.RegisterRejected, content);

    }

    private void switchToNextState() {
        if (board.getNumberOfPlayer() == 2 && stateMaschine.getCurrentState() == GameState.WaitForPlayer)
            stateMaschine.setToState(NEXT_STATE);

    }

}