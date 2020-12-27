package MyTicTacAI2.Player;

import java.util.HashMap;
import java.util.Map;
import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.Game.GameState;

public class RuleBasedAI extends Player {

    public static final String BASE_ID = "SIMPLE_AI";
    private final String gameID;
    private final static int FIELD_SIZE = 3;
    private FieldState internalRepresentation[][] = new FieldState[FIELD_SIZE][FIELD_SIZE];

    public RuleBasedAI(String id) {
        isActive = false;
        gameID = BASE_ID + "_" + id;
        for (int i = 0; i < FIELD_SIZE; i++)
            for (int n = 0; n < FIELD_SIZE; n++)
                internalRepresentation[i][n] = FieldState.Empty;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Message msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Message msg, Map<Keys, String> content) {
        if (!isActive)
            return;
        System.out.println("(AI) Recevied message: " + msg);
        for (Keys k : content.keySet())
            System.out.println("\t" + k + ":\t" + content.get(k));
        switch (msg) {
            case RegisterOpen:
                registratonOpen();
                break;
            case RegisterSuccess:
                System.out.println("Information only: " + msg.name());
                break;
            case StartGame:
                sendBasicMessage(Message.PlayerReady);
                break;
            case Set:
                set(content);
                break;
            case Turn:
                turn();
                break;
            default:
                System.out.println("Recevied not implemented message: " + msg);
                for (Keys k : content.keySet())
                    System.out.println("\t" + k + ":\t" + content.get(k));
        }
    }

    private void turn() {
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, getId());
        int middle = FIELD_SIZE / 2;
        if (internalRepresentation[middle][middle] == FieldState.Empty) {
            content.put(Keys.X, "" + middle);
            content.put(Keys.Y, "" + middle);
        }
        com.sendMessage(Message.Set, content);
    }

    private void set(Map<Keys, String> content) {
        var id = content.get(Keys.Player).equals(gameID)?FieldState.PlayerA:FieldState.PlayerB;
        var x = Integer.parseInt(content.get(Keys.X));
        var y = Integer.parseInt(content.get(Keys.Y));
        internalRepresentation[x][y] = id;
    }

    @Override
    public void start() {
        System.out.println("Starting Simple AI");
        super.start(gameID);
    }

    @Override
    public void stop() {
        System.out.println("Stopping Simple AI");
        super.stop();
    }

    @Override
    public boolean hasTurn() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clickOn(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return gameID;
    }

    @Override
    protected String getId() {
        return gameID;
    }
}
