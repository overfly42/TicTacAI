package MyTicTacAI2.Interfaces;

import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;

public interface IGameStateMachine {
    void setToState(GameState next);

    GameBoard getBoard();

    GameState getCurrentState();

    boolean isActivated();

    void startStateMaschine();

    void stopStateMaschine();
}