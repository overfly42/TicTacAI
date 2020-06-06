package MyTicTacAI2.Interfaces;

public interface IGameState {
    /**
     * Prepairs to leave the state, is called from the state machine before
     * switching to another state
     */
    void leave();

    /**
     * Called from the state maschine to enter this state
     */
    void enter();
}