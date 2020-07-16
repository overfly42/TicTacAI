package MyTicTacAI2.StateMaschine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Game.States.GameStateEndGame;

public class StateEndGameTest extends AbstractStateTest<GameStateEndGame> {

    String a = "P1";
    String b = "P2";

    /**
     * preperation of board
     * 
     * @param P1                the winner of the game
     * @param P2                the looser of the game
     * @param maxNumOfGames
     * @param currentNumOfGames
     */
    private void setBoardState(String P1, String P2, int maxNumOfGames, int currentNumOfGames) {
        board = new GameBoard();
        board.setMaxGames(maxNumOfGames);
        try {
            board.addPlayer(P1);
            board.addPlayer(P2);
        } catch (Exception e) {
        }
        board.startGame();
        for (int i = 1; i < currentNumOfGames; i++) {
            board.endGame();
            board.startGame();
        }
        assertNull(lastMessage);
        assertNull(lastStateSwitchedTo);
        board.setTurn(P1, 0, 0);
        board.setTurn(P1, 0, 1);
        board.setTurn(P1, 0, 2);
        board.setTurn(P2, 1, 0);
        board.setTurn(P2, 1, 1);
    }

    private void checkMessageWin(String player) {
        testObject = new GameStateEndGame(this, this);
        int empty = board.getWins(FieldState.Empty);
        int playerA = board.getWins(FieldState.PlayerA);
        int playerB = board.getWins(FieldState.PlayerB);
        testObject.enter();
        if (board.getPlayerFor(player) == FieldState.PlayerA) {
            assertEquals(playerA + 1, board.getWins(FieldState.PlayerA),
                    "At a Win PlayerA shall be increased, not another");
            assertEquals(playerB, board.getWins(FieldState.PlayerB), "At a Tie Ties shall be increased, not player B");
        } else {
            assertEquals(playerA, board.getWins(FieldState.PlayerA), "At a Tie Ties shall be increased, not player A");
            assertEquals(playerB, board.getWins(FieldState.PlayerB) - 1,
                    "At a Tie Ties shall be increased, not player B");
        }
        assertEquals(empty, board.getWins(FieldState.Empty), "At a Tie Ties shall be increased");
        assertEquals(Message.EndGame, lastMessage, "Information that the game has ended");
        assertTrue(lastMessageContent.containsKey(Keys.ID));
        assertEquals(player, lastMessageContent.get(Keys.ID),
                String.format("The given expected player ({0}) is not the received one ({1}).", player,
                        lastMessageContent.get(Keys.ID)));
    }

    private void checkMessageTie() {
        board.startGame();
        board.setTurn(a, 0, 0);
        board.setTurn(a, 0, 1);
        board.setTurn(b, 0, 2);
        board.setTurn(b, 1, 0);
        board.setTurn(b, 1, 1);
        board.setTurn(a, 1, 2);
        board.setTurn(a, 2, 0);
        board.setTurn(b, 2, 1);
        board.setTurn(a, 2, 2);
        testObject = new GameStateEndGame(this, this);
        int empty = board.getWins(FieldState.Empty);
        int playerA = board.getWins(FieldState.PlayerA);
        int playerB = board.getWins(FieldState.PlayerB);
        testObject.enter();
        assertEquals(playerA, board.getWins(FieldState.PlayerA), "At a Tie Ties shall be increased, not player A");
        assertEquals(playerB, board.getWins(FieldState.PlayerB), "At a Tie Ties shall be increased, not player B");
        assertEquals(empty, board.getWins(FieldState.Empty) - 1, "At a Tie Ties shall be increased");
        assertEquals(Message.EndGame, lastMessage, "Information that the game has ended");
        assertTrue(lastMessageContent.containsKey(Keys.Reason));
        assertEquals("TIE", lastMessageContent.get(Keys.Reason),
                "The correct Information has to be given about the winner");
    }

    @Test
    public void testGameEndAWins() {
        setBoardState(a, b, 5, 3);
        checkMessageWin(a);
        assertEquals(GameState.StartGame, lastStateSwitchedTo,
                "As not all games of this run are done, a new game shall be started");
    }

    @Test
    public void testGameEndBWins() {
        setBoardState(b, a, 5, 3);
        checkMessageWin(b);
        assertEquals(GameState.StartGame, lastStateSwitchedTo,
                "As not all games of this run are done, a new game shall be started");
    }

    @Test
    public void testGameEndTie() {
        setBoardState(a, b, 5, 3);
        checkMessageTie();
        assertEquals(GameState.StartGame, lastStateSwitchedTo,
                "As not all games of this run are done, a new game shall be started");
    }

    @Test
    public void testSessionEndAWins() {
        setBoardState(a, b, 5, 5);
        checkMessageWin(a);
        assertEquals(GameState.EndSession, lastStateSwitchedTo, "As there is no game left over the session is ended");
    }

    @Test
    public void testSessionEndBWins() {
        setBoardState(a, b, 5, 5);
        checkMessageWin(a);
        assertEquals(GameState.EndSession, lastStateSwitchedTo, "As there is no game left over the session is ended");
    }

    @Test
    public void testSessionEndTie() {
        setBoardState(a, b, 5, 4);
        board.endGame();
        checkMessageTie();
        assertEquals(GameState.EndSession, lastStateSwitchedTo, "As there is no game left over the session is ended");
    }
}