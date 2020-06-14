package MyTicTacAI2.Game.States;

import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;

public class GameStateInit implements IGameState {

    IGameStateMachine stateMaschine;

    public GameStateInit(IGameStateMachine gameStateMachine) {
        stateMaschine = gameStateMachine;
    }

    @Override
    public void leave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void enter() {
        stateMaschine.setToState(GameState.StartSession);
    }

}