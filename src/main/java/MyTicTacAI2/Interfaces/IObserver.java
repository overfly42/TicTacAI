package MyTicTacAI2.Interfaces;

import MyTicTacAI2.SingleFieldState;

public interface IObserver {
    void introducePlayer(IPlayer palyer);

    void startGame();

    void updateField(SingleFieldState[][] field);
}