package MyTicTacAI2.Game;

import MyTicTacAI2.Interfaces.IGameController;
import MyTicTacAI2.Interfaces.IGameStateObserver;
import MyTicTacAI2.Interfaces.IPlayer;

public class SingleGameController implements IGameController {

    private MultiGameController parent;

    public SingleGameController(MultiGameController parent) {
        this.parent = parent;

    }
    @Override
    public void executeMove(IPlayer player, int row, int col) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addGameStateObserver(IGameStateObserver observer) {
        // TODO Auto-generated method stub

    }

    /** Starts a single Game */
    public void startGame(IPlayer a,IPlayer b)
    {

    }

}