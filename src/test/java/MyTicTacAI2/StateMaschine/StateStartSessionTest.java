package MyTicTacAI2.StateMaschine;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateStartSession;

public class StateStartSessionTest extends AbstractStateTest<GameStateStartSession> {

    @Test
    public void testEnterNotReady() {
        testObject = new GameStateStartSession(this);
        assertFalse(board.areGamesOpen());
        testObject.enter();
        assertNull(lastStateSwitchedTo, "As there is no Number of Games prepaired the ready state should not be leaved");
    }
    @Test
    public void testEnterReady()
    {
        testObject = new GameStateStartSession(this);
        assertFalse(board.areGamesOpen());
        board.setMaxGames(1);
        assertTrue(board.areGamesOpen());
        testObject.enter();
        assertEquals(GameState.WaitForPlayer, lastStateSwitchedTo, "After Preperation is Done the player should be invited");
    }
    @Test
    public void testUpdate()
    {
        testObject = new GameStateStartSession(this);
        assertFalse(board.areGamesOpen());
        testObject.enter();
        assertNull(lastStateSwitchedTo, "As there is no Number of Games prepaired the ready state should not be leaved");
        board.setMaxGames(1);
        assertEquals(GameState.WaitForPlayer, lastStateSwitchedTo, "After Preperation is Done the player should be invited");    
    }

}