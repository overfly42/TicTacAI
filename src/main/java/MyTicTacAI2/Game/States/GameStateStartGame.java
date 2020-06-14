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

public class GameStateStartGame implements IGameState, IChangeListener {

    GameBoard board;
    IGameStateMachine stateMaschine;
    IComQueue queue;
    private Boolean playerA = false;
    private Boolean playerB = false;

    public GameStateStartGame(IGameStateMachine gsm, IComQueue com) {
        stateMaschine = gsm;
        queue = com;
        board = stateMaschine.getBoard();

    }

    @Override
    public void leave() {
        queue.removeListener(this);
    }

    @Override
    public void enter() {
        queue.addListener(this);
        board.startGame();
        board.switchStartPlayer();
        queue.sendMessage(Message.StartGame);
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
        if (msg != Message.PlayerReady)
            return;
        if (!content.containsKey(Keys.ID))
            return;
        playerA = playerA || board.getPlayerA().equals(content.get(Keys.ID));
        playerB = playerB || board.getPlayerB().equals(content.get(Keys.ID));
        if (playerA && playerB)
            stateMaschine.setToState(GameState.WaitForAction);
    }
}