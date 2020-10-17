package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Communication.Translator;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateStartGame;

public class StateStartGameTest extends AbstractStateTest<GameStateStartGame> {

    @Test
    public void testEnter() {
        testObject = new GameStateStartGame(this, this);
        try {
            board.addPlayer("P1");
            board.addPlayer("P2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String activeStartPlayer = board.getStartPlayer();
        assertNull(lastMessage);
        testObject.enter();
        assertNotEquals(activeStartPlayer, board.getStartPlayer(),
                "On begining of a new Game the start player should be switched");
        assertEquals(lastMessage, Message.StartGame,
                "The Server has to inform the players that a new game could be started");
        assertNull(lastStateSwitchedTo, "Only after Enter there should no switch to a new state");
        assertEquals(testObject, this.comPartner, "The Game State should register itself for receiving new messages");
    }
    @Test
    public void testPlayerOneReady() {
        testEnter();
        String message = "PlayerReady:ID=P1";
        Map<Keys, String> content = new HashMap<>();
        Message msg = Translator.fromQueue(message, content);
        testObject.update(msg, content);
        assertNull(lastStateSwitchedTo, "Only after Enter there should no switch to a new state");
    }
    @Test
    public void testPlayerTwoReady(){
        testPlayerOneReady();
        String message = "PlayerReady:ID=P2";
        Map<Keys, String> content = new HashMap<>();
        Message msg = Translator.fromQueue(message, content);
        testObject.update(msg, content);
        assertEquals(GameState.WaitForAction,lastStateSwitchedTo, "Only after Enter there should no switch to a new state");    
    }
    @Test
    public void testPlayerOneDouble()
    {
        testEnter();
        String message = "PlayerReady:P1";
        Map<Keys, String> content = new HashMap<>();
        Message msg = Translator.fromQueue(message, content);
        testObject.update(msg, content);
        assertNull(lastStateSwitchedTo, "Only after Enter there should no switch to a new state");
    }
    @Test 
    public void testPlayerThree()
    {
        testEnter();
        String message = "PlayerReady:P3";
        Map<Keys, String> content = new HashMap<>();
        Message msg = Translator.fromQueue(message, content);
        testObject.update(msg, content);
        assertNull(lastStateSwitchedTo, "Only after Enter there should no switch to a new state");

    }
}