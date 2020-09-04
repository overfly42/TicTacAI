package MyTicTacAI2.Game.States;

import java.util.HashMap;
import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IComQueue;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;

public class GameStateWaitForAction implements IGameState, IChangeListener {

    IGameStateMachine gameStateMachine;
    GameBoard board;
    IComQueue com;
    private String playerId;

    public GameStateWaitForAction(IGameStateMachine stateMachine, IComQueue queue) {
        gameStateMachine = stateMachine;
        board = gameStateMachine.getBoard();
        com = queue;

    }

    @Override
    public void leave() {
        com.removeListener(this);
    }

    /**
     * On Enter of this state there shall be an message out to the player who'S turn
     * it is
     */
    @Override
    public void enter() {
        com.addListener(this);
        Message msg = Message.Turn;
        playerId = board.getActivePlayer();
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, playerId);
        com.sendMessage(msg, content);
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
        if (msg != Message.Set)
            return;
        if (!content.containsKey(Keys.ID))
            return;
        if (!content.containsKey(Keys.X))
            return;
        if (!content.containsKey(Keys.Y))
            return;
        int x = Integer.parseInt(content.get(Keys.X));
        int y = Integer.parseInt(content.get(Keys.Y));
        // Happy Path
        if (board.getStateOfField(x, y) == FieldState.Empty && content.get(Keys.ID).equals(playerId)) {
            board.setTurn(content.get(Keys.ID), x, y);
            content.put(Keys.Player, content.get(Keys.ID));
            content.put(Keys.ID, "all");
            com.sendMessage(Message.Set,content);
            board.switchActivePlayer();
            gameStateMachine.setToState(GameState.CheckField);
        }
        // Error Path
        else {
            Message returnMessage = Message.SetRejected;
            content.put(Keys.Reason, "Field not empty");
            com.sendMessage(returnMessage, content);
        }
    }

}