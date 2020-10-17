package MyTicTacAI2.Game.States;

import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IChangeListener;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;

public class GameStateStartSession implements IGameState, IChangeListener {

    IGameStateMachine stateMaschine;
    GameBoard board;

    public GameStateStartSession(IGameStateMachine gameStateMaschine) {
        stateMaschine = gameStateMaschine;
        board = stateMaschine.getBoard();
        board.addChangeListener(this);
    }

    @Override
    public void leave() {
        board.removeChangeListner(this);
    }

    @Override
    public void enter() {
        board.startSession();
        if (board.areGamesOpen())
            stateMaschine.setToState(GameState.WaitForPlayer);
    }

    @Override
    public void update() {
        if (board.areGamesOpen())
            stateMaschine.setToState(GameState.WaitForPlayer);

    }

    @Override
    public void update(Message msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Message msg, Map<Keys, String> content) {
        // TODO Auto-generated method stub

    }

}