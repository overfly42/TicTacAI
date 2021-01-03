package MyTicTacAI2.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

public class HumanPlayer extends Player {

    List<SimpleStringProperty> field;
    SimpleStringProperty player;
    Function<String, String> messagePathToUser;
    private boolean hasTurn;

    public HumanPlayer(List<SimpleStringProperty> fieldView, SimpleStringProperty playerProperty,
            Function<String, String> textAppender) {
        field = fieldView;
        player = playerProperty;
        messagePathToUser = textAppender;
        hasTurn = false;
        isActive = false;
    }

    @Override
    public void start() {
        start(player.getValue());
    }

    @Override
    public void update() {
        System.out.println("Update called");
    }

    @Override
    public void update(Message msg) {
        System.out.println("Update: " + msg);
    }

    @Override
    public void update(Message msg, Map<Keys, String> content) {
        if (!isActive) // Nothing to do
            return;
        System.out.println(String.format("Update for %s: %s", player.get(), msg));
        for (Keys k : content.keySet())
            System.out.println("\t" + k.toString() + "\t= " + content.get(k));
        switch (msg) {
            case RegisterOpen:
                registratonOpen();
                break;
            case StartGame:
                startGame();
                break;
            case Turn:
                trun();
                break;
            case Set:
                set(content);
                break;
            case EndSession:
                endSession(content);
                break;
            case EndGame:
                endGame();
                break;
            case RegisterSuccess:
                System.out.println(String.format("Message %s arrived, information only", msg.toString()));
                messagePathToUser.apply(String.format("Player ID for %s is %s\n", player.get(),content.get(Keys.Player)));
                break;
            case PlayerReady:
            case Register:
            case RegisterRejected:
            case SetRejected:
            case StartSession:
                System.out.println(String.format("Message %s not implemented yet", msg.toString()));
                break;
            default:
                break;

        }
    }

    private void endGame() {
        messagePathToUser.apply("Game Ended\n");
        hasTurn = false;
        Platform.runLater(() -> {
            for (SimpleStringProperty tile : field) {
                tile.set("");
            }
        });
    }

    private void endSession(Map<Keys, String> content) {
        String message = String.format("Statistics:\n A=%s\n B=%s\n Tie=%s\n", content.get(Keys.PlayerA),
                content.get(Keys.PlayerB), content.get(Keys.Tie));
        messagePathToUser.apply(message);
    }

    private void set(Map<Keys, String> content) {
        int x = Integer.parseInt(content.get(Keys.X));
        int y = Integer.parseInt(content.get(Keys.Y));
        int listIndex = x * 3 + y;
        Platform.runLater(() -> {
            field.get(listIndex).set(content.get(Keys.Player));
        });
    }

    private void trun() {
        hasTurn = true;
        messagePathToUser.apply(String.format("Your Turn %s\n", player.get()));
    }

    private void startGame() {
        sendBasicMessage(Message.PlayerReady);
    }

    public boolean hasTurn() {
        return hasTurn;
    }

    public void clickOn(int x, int y) {
        hasTurn = false;
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, player.get());
        content.put(Keys.X, Integer.toString(x));
        content.put(Keys.Y, Integer.toString(y));
        com.sendMessage(Message.Set, content);
    }

    @Override
    public String toString() {
        return "HumanPlayer";
    }

    @Override
    protected String getId() {
        return player.get();
    }
}
