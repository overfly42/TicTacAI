package MyTicTacAI2.Game;

import java.util.HashMap;
import java.util.Map;

import MyTicTacAI2.Game.States.*;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMaschine;

public class GameStateMachine implements IGameStateMaschine{
    private Map<GameState, IGameState> states;
    private IGameState currentState;

    public GameStateMachine() {
        states = new HashMap<>();
        states.put(GameState.CheckField,new GameStateCheckField());
        states.put(GameState.EndGame,new GameStateEndGame());
        states.put(GameState.EndSession,new GameStateEndSession());
        states.put(GameState.Init,new GameStateInit(this));
        states.put(GameState.Preperation,new GameStatePreperation());
        states.put(GameState.Ready,new GameStateReady());
        states.put(GameState.StartGame,new GameStateStartGame());
        states.put(GameState.StartSession,new GameStateStartSession());
        states.put(GameState.WaitForAction,new GameStateWaitForAction());
        states.put(GameState.WaitForPlayer,new GameStateWaitForPlayer());

        currentState = states.get(GameState.Init);
    }

    @Override
    public void setToState(GameState next) {
        currentState.leave();
        currentState =states.get(next);
        currentState.enter();

    }
}