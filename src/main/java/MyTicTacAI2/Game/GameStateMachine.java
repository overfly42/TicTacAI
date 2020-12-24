package MyTicTacAI2.Game;

import java.util.HashMap;
import java.util.Map;

import MyTicTacAI2.Game.States.*;
import MyTicTacAI2.Interfaces.IComQueue;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;

public class GameStateMachine implements IGameStateMachine {
    private final Map<GameState, IGameState> states;
    private IGameState currentState;
    private final GameBoard board;
    private IComQueue queue;
    private boolean stateMaschineActivationState;

    public GameStateMachine(IComQueue com) {
        queue = com;
        board = new GameBoard();

        states = new HashMap<>();
        // states.put(GameState.CheckField, new GameStateCheckField());
        // states.put(GameState.EndGame, new GameStateEndGame());
        // states.put(GameState.EndSession, new GameStateEndSession());
        states.put(GameState.Init, new GameStateInit(this));
        states.put(GameState.StartSession, new GameStateStartSession(this));
        states.put(GameState.WaitForPlayer, new GameStateWaitForPlayer(this, queue));
        states.put(GameState.StartGame, new GameStateStartGame(this, queue));
        states.put(GameState.WaitForAction, new GameStateWaitForAction(this, queue));
        states.put(GameState.CheckField, new GameStateCheckField(this));
        states.put(GameState.EndGame, new GameStateEndGame(this, queue));
        states.put(GameState.EndSession, new GameStateEndSession(this, queue));
        // states.put(GameState.StartGame, new GameStateStartGame());
        // states.put(GameState.WaitForAction, new GameStateWaitForAction());

        stateMaschineActivationState = false;
    }

    @Override
    public void setToState(final GameState next) {
        System.out.println("Switchting from " + currentState.toString() +"to State: " + next.name());
        if (currentState != null)
            currentState.leave();
        currentState = states.get(next);
        currentState.enter();

    }

    @Override
    public GameBoard getBoard() {

        return board;
    }

    @Override
    public GameState getCurrentState() {
        for (var pair : states.entrySet())
            if (pair.getValue() == currentState)
                return pair.getKey();
        return null;
    }

    @Override
    public boolean isActivated() {
        return stateMaschineActivationState;
    }

    @Override
    public void startStateMaschine() {
        if (stateMaschineActivationState)
            return;
        stateMaschineActivationState = true;
        currentState = states.get(GameState.Init);
        currentState.enter();
    }

    @Override
    public void stopStateMaschine() {
        stateMaschineActivationState = false;
    }
}