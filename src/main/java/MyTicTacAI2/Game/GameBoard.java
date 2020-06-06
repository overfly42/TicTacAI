package MyTicTacAI2.Game;

public class GameBoard {
    private static GameBoard instance;

    public static GameBoard getInstance() {
        if (instance == null)
            instance = new GameBoard();
        return instance;
    }
    
}