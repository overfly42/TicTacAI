package MyTicTacAI2.Interfaces;

import java.util.List;

public interface IGameController {
    void executeMove(IPlayer player, int row, int col);
    void addGameStateObserver(IGameStateObserver observer);
    void startGame(IPlayer playerA,IPlayer playerB,List<IObserver> observer);
}