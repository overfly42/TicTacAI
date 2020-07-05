package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateCheckField;

public class StateCheckFieldTest extends AbstractStateTest<GameStateCheckField> {
    String a = "P1";
    String b = "P2";

    private void setUpPlayers()
    {
        try {
            board.addPlayer(a);
            board.addPlayer(b);
        } catch (Exception e) {
            assertNotNull(null, "There shall be no exception onadding players");
        }
        
    }

    @Test
    public void testFinishTie() {
        setUpPlayers();
        board.startGame();
        board.setTurn(a, 0, 0);
        board.setTurn(b, 0, 1);
        board.setTurn(b, 0, 2);
        board.setTurn(b, 1, 0);
        board.setTurn(a, 1, 1);
        board.setTurn(a, 1, 2);
        board.setTurn(a, 2, 0);
        board.setTurn(a, 2, 1);
        board.setTurn(b, 2, 2);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.EndGame, lastStateSwitchedTo, "If no move is possible, game finishes");
    }

    @Test
    public void testFinishPlayerAWin() {
        setUpPlayers();
        board.startGame();
        board.setTurn(a, 0, 0);
        board.setTurn(a, 1, 1);
        board.setTurn(a, 2, 2);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 1, 0);
        board.setTurn(b, 2, 0);
        board.setTurn(b, 2, 1);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.EndGame, lastStateSwitchedTo, "If one has won, game finishes");
    
    }

    @Test
    public void testFinishPlayerBWin() {
setUpPlayers();
        board.startGame();
        board.setTurn(a, 1, 1);
        board.setTurn(a, 2, 2);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 1, 0);
        board.setTurn(b, 2, 0);
        board.setTurn(b, 0, 0);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.EndGame, lastStateSwitchedTo, "If one has won, game finishes");
    
    }

    @Test
    public void testContinueGameOneSet() {
        setUpPlayers();
        board.startGame();
        board.setTurn(a, 1, 1);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
    }

    @Test
    public void testContinueGameTwoSet() {
        setUpPlayers();
        board.startGame();
        board.setTurn(a, 1, 1);
        board.setTurn(b, 0, 0);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
    }

    @Test
    public void testContinueGameThreeSet() {
        setUpPlayers();
        board.startGame();
        board.setTurn(b, 0, 0);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 0, 2);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
    }

    @Test
    public void testContinueGameFourSet() {
        setUpPlayers();
        board.startGame();
        board.setTurn(b, 0, 0);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 0, 2);
        board.setTurn(b, 1, 0);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
    }

    @Test
    public void testContinueGameFiveSet() {
        setUpPlayers();
        board.startGame();
        board.setTurn(b, 0, 0);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 0, 2);
        board.setTurn(b, 1, 0);
        board.setTurn(a, 1, 1);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
    }

    @Test
    public void testContinueGameSixSet() {
        setUpPlayers();
        board.startGame();
        board.setTurn(b, 0, 0);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 0, 2);
        board.setTurn(b, 1, 0);
        board.setTurn(a, 1, 1);
        board.setTurn(a, 1, 2);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
    }

    @Test
    public void testContinueGameSevenSet() {
            setUpPlayers();
            board.startGame();
            board.setTurn(b, 0, 0);
            board.setTurn(a, 0, 1);
            board.setTurn(b, 0, 2);
            board.setTurn(b, 1, 0);
            board.setTurn(a, 1, 1);
            board.setTurn(a, 1, 2);
            board.setTurn(a, 2, 0);
                testObject = new GameStateCheckField(this);
            testObject.enter();
            assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
        }

    @Test
    public void testContinueGameEightSet() {
        setUpPlayers();
        board.startGame();
        /*
        bab
        baa
        aba
        */
        board.setTurn(b, 0, 0);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 0, 2);
        board.setTurn(b, 1, 0);
        board.setTurn(a, 1, 1);
        board.setTurn(a, 1, 2);
        board.setTurn(a, 2, 0);
        board.setTurn(b, 2, 1);
        testObject = new GameStateCheckField(this);
        testObject.enter();
        assertEquals(GameState.WaitForAction, lastStateSwitchedTo, "If no one has one and there are possible moves left the game goes on");
}

}