package MyTicTacAI2.Interfaces;

import MyTicTacAI2.GameState;

public interface IGameStateObserver {
void switchedGameStateTo(GameState newState);
}
