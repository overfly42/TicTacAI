package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Communication.Translator;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateWaitForPlayer;

public class StateWaitForPlayerTest extends AbstractStateTest<GameStateWaitForPlayer> {


    @Test
    public void testEnterNoPlayerRegisteredYet() {
        testObject = new GameStateWaitForPlayer(this, this);
        lastStateSwitchedTo = GameState.WaitForPlayer;
        assertEquals(testObject, this.comPartner,
                "The waiting state should have attached itself to rabbit MQ to listen the incomming trafic");
        assertFalse(stateSwitchCalled);
        testObject.enter();
        assertFalse(stateSwitchCalled);
        assertEquals(Message.RegisterOpen, lastMessage, "The Player should be informed that the regestration is open.");
    }

    @Test
    public void testFirstPlayer() {
        testEnterNoPlayerRegisteredYet();
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, "P1");
        comPartner.update(Message.Register, content);
        assertNull(board.getPlayerB());
        assertNotNull(board.getPlayerA());
        assertEquals("P1", board.getPlayerA(), "First player has be be registerted right now");
        assertEquals(1, board.getNumberOfPlayer(), "Only One Player is registered by now");
    }

    @Test
    public void testFirstPlayerDouble() {
        testFirstPlayer();
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, "P1");
        comPartner.update(Message.Register, content);
        assertNull(board.getPlayerB());
        assertEquals("P1", board.getPlayerA(), "First player should not change");
        assertEquals(Message.RegisterRejected, lastMessage,
                "The regestriation of a player with already used ID is not allowed");
    }

    @Test
    public void testSecondPlayer() {
        testFirstPlayer();
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, "P2");
        comPartner.update(Message.Register, content);
        assertNotNull(board.getPlayerB());
        assertEquals("P2", board.getPlayerB(), "Second player should be registerted");
        assertTrue(stateSwitchCalled);
        assertEquals(GameState.StartSession, lastStateSwitchedTo,
                "After player selection the wait for player state is over");
        
    }

    @Test
    public void testThirdPlayerRejection() {
        testSecondPlayer();
        lastStateSwitchedTo = null;
        stateSwitchCalled = false;
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, "P3");
        comPartner.update(Message.Register, content);
        assertEquals(Message.RegisterRejected, lastMessage, "The regestriation of a third player is prohibited");
        assertFalse(stateSwitchCalled);
        assertNull(lastStateSwitchedTo, "in rejection only mode the state should be changed");

        String exprectedMessage = "P3:RegisterRejected";
        assertEquals(exprectedMessage, Translator.toQueue(lastMessage, content), "Message shold be as expected");
    }

    @Override
    public GameState getCurrentState() {
        return lastStateSwitchedTo;
    }
}