package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateEndSession;

public class StateEndSessionTest extends AbstractStateTest<GameStateEndSession> {

    @Test
    public void testEnter() {
        String a = "P1";
        String b = "P2";
        board = new GameBoard();
        try {
            board.addPlayer(a);
            board.addPlayer(b);
        } catch (Exception e) {
            // TODO: handle exception
        }
        board.startGame();
        board.endGame();
        int ties = board.getWins(FieldState.Empty);
        int pa = board.getWins(FieldState.PlayerA);
        int pb = board.getWins(FieldState.PlayerB);
        testObject = new GameStateEndSession(this, this);
        testObject.enter();
        assertEquals(Message.EndSession, lastMessage, "Message that the Session is terminated");
        assertTrue(lastMessageContent.containsKey(Keys.PlayerA));
        assertTrue(lastMessageContent.containsKey(Keys.PlayerB));
        assertTrue(lastMessageContent.containsKey(Keys.Tie));
        assertEquals("" + ties, lastMessageContent.get(Keys.Tie));
        assertEquals("" + pa, lastMessageContent.get(Keys.PlayerA));
        assertEquals("" + pb, lastMessageContent.get(Keys.PlayerB));
        assertEquals(GameState.Init, lastStateSwitchedTo, "After the session is ended, the cylce should begin again");
    }
}