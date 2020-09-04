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

public class GameStateEndSession implements IGameState {

    IGameStateMachine gameStateMachine;
    IComQueue com;
    GameBoard board;

    public GameStateEndSession(IGameStateMachine stateMaschine, IComQueue queue) {
        gameStateMachine = stateMaschine;
        com = queue;
        board = gameStateMachine.getBoard();
    }

    @Override
    public void leave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void enter() {
        Map<Keys,String> content = new HashMap<>();
        content.put(Keys.PlayerA,""+board.getWins(FieldState.PlayerA));
        content.put(Keys.PlayerB,""+board.getWins(FieldState.PlayerB));
        content.put(Keys.Tie,""+board.getWins(FieldState.Empty));
        content.put(Keys.ID, "all");
        com.sendMessage(Message.EndSession,content);
//        gameStateMachine.setToState(GameState.Init);
    }

}