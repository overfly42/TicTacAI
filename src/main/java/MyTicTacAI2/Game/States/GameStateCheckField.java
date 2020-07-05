package MyTicTacAI2.Game.States;

import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;
import MyTicTacAI2.utils.FieldCalculator;

public class GameStateCheckField implements IGameState {
    IGameStateMachine gameStateMachine;
    GameBoard board;

    public GameStateCheckField(IGameStateMachine statemaschine) {
        gameStateMachine = statemaschine;
        board = gameStateMachine.getBoard();
    }

    @Override
    public void leave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void enter() {
        GameState nextState = null;
        if (FieldCalculator.gameOver(board.getFieldCopy())) {
            nextState = GameState.EndGame;
        } else {
            nextState = GameState.WaitForAction;
        }
        gameStateMachine.setToState(nextState);
    }

}