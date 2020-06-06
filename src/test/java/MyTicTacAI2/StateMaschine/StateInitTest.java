package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateInit;
import MyTicTacAI2.Game.States.GameStateReady;
import MyTicTacAI2.Interfaces.IComQueue;
import MyTicTacAI2.Interfaces.IGameStateMaschine;

public class StateInitTest implements IComQueue, IGameStateMaschine {

    private GameState lastStateSwitchedTo = null;

    @Override
    public void setToState(GameState next) {
        lastStateSwitchedTo = next;
    }

    @Test
    public void testEnter() {
        GameStateInit testObject = new GameStateInit(this);
        testObject.enter();
        assertEquals(GameState.Ready, lastStateSwitchedTo,
                "As there is nothing to do, from init should a transistion to ready take place imidiatly.");
    }

}