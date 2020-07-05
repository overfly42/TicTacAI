package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Communication.Translator;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateWaitForAction;

public class StateWaitForActionTest extends AbstractStateTest<GameStateWaitForAction> {
    @Test
    public void testEnter() {
        this.testObject = new GameStateWaitForAction(this, this);
        assertNull(lastStateSwitchedTo);
        try {
            board.addPlayer("P1");
            board.addPlayer("P2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // board.getActivePlayer()
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, board.getActivePlayer());
        String expected = Translator.toQueue(Message.Turn, content);
        assertNull(comPartner, "The com Partner shall not be set right now");
        testObject.enter();
        assertEquals(testObject, comPartner, "After enter the state has to had registered itself for updates");
        assertNotNull(lastMessage, "By now a message to start has to been sended");
        assertEquals(expected, Translator.toQueue(lastMessage, lastMessageContent),
                "On Entry this state has to comunicaate, that it is waiting for an response of a specific player");

        assertNull(lastStateSwitchedTo, "By know there should be no state change enabled");
    }

    @Test
    public void testLeave() {
        testEnter();
        assertEquals(testObject, comPartner, "After Enter of state it shall listen to the communication queue");
        testObject.leave();
        assertNull(comPartner, "If not active, the state shall not react on any message");
    }

    @Test
    public void testReceiveCorrectActionFromCorrectPlayerEmptyField() {
        testEnter();
        board.startGame();
        Message msg = Message.Set;
        Map<Keys, String> content = new HashMap<>();
        String playerId = lastMessageContent.get(Keys.ID);
        content.put(Keys.ID, playerId);
        content.put(Keys.X, "1");
        content.put(Keys.Y, "1");
        FieldState currentFieldState = board.getStateOfField(1, 1);
        assertEquals(FieldState.Empty, currentFieldState, "The Test field should be empty and for this be allowed");
        testObject.update(msg, content);
        currentFieldState = board.getStateOfField(1, 1);
        FieldState expectedPlayer = board.getPlayerFor(playerId);
        assertEquals(expectedPlayer, currentFieldState, "As the Move was allowed, field should be set");
        assertEquals(GameState.CheckField, lastStateSwitchedTo,
                "After a allowed turn, the current field combination has to be evaluated");
        assertNotEquals(playerId, board.getActivePlayer(), "After a correct move the next player should be set to active");
    }

    @Test
    public void testReceiveCorrectActionFromCorrectPlayerNotEmptyField() {
        testEnter();
        board.startGame();
        Message msg = Message.Set;
        Map<Keys, String> content = new HashMap<>();
        String playerId = lastMessageContent.get(Keys.ID);
        content.put(Keys.ID, playerId);
        content.put(Keys.X, "1");
        content.put(Keys.Y, "1");
        FieldState expectedPlayer = board.getPlayerFor(playerId);
        String oponent;
        if (board.getPlayerA().equals(playerId))
            oponent = board.getPlayerB();
        else
            oponent = board.getPlayerA();
        board.setTurn(oponent, 0, 0);
        FieldState currentFieldState = board.getStateOfField(1, 1);
        assertEquals(FieldState.Empty, currentFieldState, "The Test field should be empty and for this be allowed");
        testObject.update(msg, content);
        currentFieldState = board.getStateOfField(1, 1);
        assertEquals(expectedPlayer, currentFieldState, "As the Move was allowed, field should be set");
        assertEquals(GameState.CheckField, lastStateSwitchedTo,
                "After a allowed turn, the current field combination has to be evaluated");
        assertEquals(oponent, board.getActivePlayer(), "After a correct move the next player should be set to active");
    }

    @Test
    public void testReceiveCorrectActionFromWrongPlayer() {
        testEnter();
        board.startGame();
        Message msg = Message.Set;
        Map<Keys, String> content = new HashMap<>();
        String playerId = lastMessageContent.get(Keys.ID);
        content.put(Keys.ID, playerId);
        content.put(Keys.X, "1");
        content.put(Keys.Y, "1");
        String oponent;
        if (board.getPlayerA().equals(playerId))
            oponent = board.getPlayerB();
        else
            oponent = board.getPlayerA();
        board.setTurn(oponent, 0, 0);
        content.put(Keys.ID, oponent);
        FieldState currentFieldState = board.getStateOfField(1, 1);
        assertEquals(FieldState.Empty, currentFieldState,
                "The Test field should be free, as this the trun basicly would be correct");
        testObject.update(msg, content);
        assertNull(lastStateSwitchedTo, "As there was an error the state should not be left");
        assertEquals(Message.SetRejected, lastMessage, "The Set Operation should be rejected as the move was invalid");
        assertEquals(oponent, lastMessageContent.get(Keys.ID),
                "The rejection should to the same player as the action was recevied from");
        assertTrue(lastMessageContent.containsKey(Keys.Reason), "A Reason should be given on a rejection");
    }

    @Test
    public void testReceiveWrongActionFromCorrectPlayer() {
        testEnter();
        board.startGame();
        Message msg = Message.Set;
        Map<Keys, String> content = new HashMap<>();
        String playerId = lastMessageContent.get(Keys.ID);
        content.put(Keys.ID, playerId);
        content.put(Keys.X, "1");
        content.put(Keys.Y, "1");
        String oponent;
        if (board.getPlayerA().equals(playerId))
            oponent = board.getPlayerA();
        else
            oponent = board.getPlayerB();
        board.setTurn(oponent, 1, 1);
        FieldState currentFieldState = board.getStateOfField(1, 1);
        assertNotEquals(FieldState.Empty, currentFieldState,
                "The Test field is already in use and is not allowed to set there");
        testObject.update(msg, content);
        assertNull(lastStateSwitchedTo, "As there was an error the state should not be left");
        assertEquals(Message.SetRejected, lastMessage, "The Set Operation should be rejected as the move was invalid");
        assertEquals(playerId, lastMessageContent.get(Keys.ID),
                "The rejection should to the same player as the action was recevied from");
        assertTrue(lastMessageContent.containsKey(Keys.Reason), "A Reason should be given on a rejection");
    }

    @Test
    public void testReceiveWrongActionFromWrongPlayer() {
        testEnter();
        board.startGame();
        Message msg = Message.Set;
        Map<Keys, String> content = new HashMap<>();
        String playerId = lastMessageContent.get(Keys.ID);
        content.put(Keys.ID, playerId);
        content.put(Keys.X, "1");
        content.put(Keys.Y, "1");
        String oponent;
        if (board.getPlayerA().equals(playerId))
            oponent = board.getPlayerB();
        else
            oponent = board.getPlayerA();
        board.setTurn(oponent, 1, 1);
        content.put(Keys.ID, oponent);
        FieldState currentFieldState = board.getStateOfField(1, 1);
        assertNotEquals(FieldState.Empty, currentFieldState,
                "The Target field should not be empty");
        testObject.update(msg, content);
        assertNull(lastStateSwitchedTo, "As there was an error the state should not be left");
        assertEquals(Message.SetRejected, lastMessage, "The Set Operation should be rejected as the move was invalid");
        assertEquals(oponent, lastMessageContent.get(Keys.ID),
                "The rejection should to the same player as the action was recevied from");
        assertTrue(lastMessageContent.containsKey(Keys.Reason), "A Reason should be given on a rejection");
   }
}