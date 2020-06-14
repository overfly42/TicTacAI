package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateInit;

public class StateInitTest extends AbstractStateTest {

    @Test
    public void testEnter() {
        GameStateInit testObject = new GameStateInit(this);
        testObject.enter();
        assertEquals(GameState.StartSession, lastStateSwitchedTo,
                "As there is nothing to do, from init should a transistion to ready take place imidiatly.");
    }


}