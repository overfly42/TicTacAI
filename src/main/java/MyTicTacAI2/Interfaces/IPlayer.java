package MyTicTacAI2.Interfaces;

public interface IPlayer extends IObserver
{
    boolean isReadyToStart();
    void startAction();
    void registerController(IGameController gameController);
    void unregisterController(IGameController gameController);
    String getId();
}