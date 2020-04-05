package MyTicTacAI2.Interfaces;

public interface IPlayer extends IObserver
{
    /**Determites if the player is able to receive a startAction Commant */
    boolean isReadyToStart();
    /** Starts the turn of this player */
    void startAction();
    void registerController(IGameController gameController);
    void unregisterController(IGameController gameController);
    String getId();
}