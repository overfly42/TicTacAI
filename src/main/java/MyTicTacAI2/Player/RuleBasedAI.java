package MyTicTacAI2.Player;

import java.util.Map;
import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.GameState;

public class RuleBasedAI extends Player {

    public static final String BASE_ID = "SIMPLE_AI";
    private final String gameID;

    public RuleBasedAI(String id) {
        isActive = false;
        gameID = BASE_ID + "_" + id;
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
            default:
                System.out.println("Recevied not implemented message: " + msg);
                for (Keys k : content.keySet())
                    System.out.println("\t" + k + ":\t" + content.get(k));
        }
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
