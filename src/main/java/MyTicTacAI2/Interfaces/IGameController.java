package MyTicTacAI2.Interfaces;

public interface IGameController {
    void executeMove(IPlayer player, int row, int col);
    void addGameStateObserver(IGameStateObserver observer);
    
}