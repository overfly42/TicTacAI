package MyTicTacAI2.Game.States;

import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMaschine;

public class GameStateInit implements IGameState {

    IGameStateMaschine stateMaschine;

    public GameStateInit(IGameStateMaschine gameStateMachine) {
        stateMaschine = gameStateMachine;
    }

    @Override
    public void leave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void enter() {
        stateMaschine.setToState(GameState.Ready);
    }

}