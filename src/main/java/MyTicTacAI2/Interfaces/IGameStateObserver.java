package MyTicTacAI2.Interfaces;

import MyTicTacAI2.Game.GameExecutionState;

public interface IGameStateObserver {
    public void switchedGameStateTo(GameExecutionState newState);
}
