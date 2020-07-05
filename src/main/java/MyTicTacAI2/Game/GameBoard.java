package MyTicTacAI2.Game;

import java.util.ArrayList;
import java.util.List;

import MyTicTacAI2.Interfaces.IChangeListener;

public class GameBoard {

    private static final int FIELD_SIZE = 3;
    private int maxGames;
    private int currentGame;
    private List<IChangeListener> observer;
    private String playerA;
    private String playerB;
    private FieldState[][] board;
    private boolean startPlayer;
    private boolean activePlayer;

    public GameBoard() {
        startSession();
        startPlayer = true;
    }

    public int getMaxGames() {
        return maxGames;
    }

    public void setMaxGames(int max) {
        maxGames = max;
        updateObserver();
    }

    public void startGame() {
        currentGame++;
        updateObserver();
        board = new FieldState[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++)
            for (int j = 0; j < FIELD_SIZE; j++)
                board[i][j] = FieldState.Empty;
    }

    public void startSession() {
        maxGames = 0;
        currentGame = 0;
        observer = new ArrayList<>();
        playerA = null;
        playerB = null;
    }

    public boolean areGamesOpen() {
        return currentGame < maxGames;
    }

    public void addChangeListener(IChangeListener listener) {
        observer.add(listener);
    }

    public void removeChangeListner(IChangeListener listner) {
        observer.remove(listner);
    }

    private void updateObserver() {
        for (IChangeListener listner : observer) {
            listner.update();
        }
    }

    public void addPlayer(String name) throws Exception {
        if (playerA == null)
            playerA = name;
        else if (playerB == null)
            playerB = name;
        else
            throw new Exception("Both players are already registered");
    }

    public FieldState getPlayerFor(String playerName) {
        if (playerA.equals(playerName))
            return FieldState.PlayerA;
        if (playerB.equals(playerName))
            return FieldState.PlayerB;
        return FieldState.Empty;
    }

    public int getNumberOfPlayer() {
        int cnt = 0;
        cnt += playerA == null ? 0 : 1;
        cnt += playerB == null ? 0 : 1;
        return cnt;
    }

    public String getPlayerA() {
        return playerA;
    }

    public String getPlayerB() {
        return playerB;
    }

    public String getStartPlayer() {
        return startPlayer ? playerA : playerB;
    }

    public void switchStartPlayer() {
        startPlayer = !startPlayer;
        activePlayer = startPlayer;
    }

    public String getActivePlayer() {
        return activePlayer ? playerA : playerB;
    }

    public void switchActivePlayer() {
        activePlayer = !activePlayer;
    }

    public FieldState getStateOfField(int x, int y) {
        if (!bordersMet(x, y))
            return null;
        return board[x][y];
    }

    public boolean setTurn(String player, int x, int y) {
        if (!bordersMet(x, y))
            return false;
        if (board[x][y] != FieldState.Empty)
            return false;
        board[x][y] = getPlayerFor(player);
        return true;
    }

    private boolean bordersMet(int x, int y) {
        if (x < 0 || x >= board.length)
            return false;
        if (y < 0 || y >= board[0].length)
            return false;
        return true;
    }
    public FieldState[][] getFieldCopy()
    {
        return board.clone();
    }
}
