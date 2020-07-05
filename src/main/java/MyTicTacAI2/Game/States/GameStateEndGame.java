package MyTicTacAI2.Game.States;

import java.util.HashMap;
import java.util.Map;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.Game.GameBoard;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.Interfaces.IComQueue;
import MyTicTacAI2.Interfaces.IGameState;
import MyTicTacAI2.Interfaces.IGameStateMachine;
import MyTicTacAI2.utils.FieldCalculator;

public class GameStateEndGame implements IGameState {

    IGameStateMachine gameStateMaschine;
    IComQueue com;
    GameBoard board;

    public GameStateEndGame(IGameStateMachine stateMachine, IComQueue queue) {
        gameStateMaschine = stateMachine;
        board = stateMachine.getBoard();
        com = queue;
    }

    @Override
    public void leave() {

    }

    @Override
    public void enter() {
        var winner = FieldCalculator.getWinner(board.getFieldCopy());
        Keys key;
        String text;
        if (winner == FieldState.Empty) {
            key = Keys.Reason;
            text = "TIE";
        } else {
            key = Keys.ID;
            text = winner == FieldState.PlayerA ? board.getPlayerA() : board.getPlayerB();
        }
        Map<Keys, String> content = new HashMap<>();
        content.put(key, text);
        com.sendMessage(Message.EndGame, content);
        gameStateMaschine.setToState(board.areGamesOpen() ? GameState.StartGame : GameState.EndSession);
    }

}