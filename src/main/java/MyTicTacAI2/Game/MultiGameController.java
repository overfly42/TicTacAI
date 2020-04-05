package MyTicTacAI2.Game;

import java.util.List;

import MyTicTacAI2.Interfaces.IGameController;
import MyTicTacAI2.Interfaces.IGameStateObserver;
import MyTicTacAI2.Interfaces.IObserver;
import MyTicTacAI2.Interfaces.IPlayer;

public class MultiGameController implements IGameController {
    /**
     * 
     * @param a             Class for Player A
     * @param b             Class for Player B
     * @param observer      List of Observer that should be informed of moves
     * @param numberOfGames The number of games that should run in a row
     */
    public void startGame(Class<? extends IPlayer> a, Class<? extends IPlayer> b,
            List<Class<? extends IObserver>> observer, int numberOfGames) {
        System.out.println(String.format("Try to start %d Game(s) between %s and %s", numberOfGames, a.getSimpleName(),
                b.getSimpleName()));
    }

    @Override
    public void executeMove(IPlayer player, int row, int col) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addGameStateObserver(IGameStateObserver observer) {
        // TODO Auto-generated method stub

    }
}