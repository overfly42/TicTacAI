package MyTicTacAI2.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.Game.GameState;
import MyTicTacAI2.utils.FieldCalculator;

public class RuleBasedAI extends Player {

    public static final String BASE_ID = "SIMPLE_AI";
    private final String gameID;
    private final static int FIELD_SIZE = 3;
    private FieldState internalRepresentation[][] = new FieldState[FIELD_SIZE][FIELD_SIZE];
    private java.util.Random random;

    public RuleBasedAI(String id) {
        isActive = false;
        gameID = BASE_ID + "_" + id;
        for (int i = 0; i < FIELD_SIZE; i++)
            for (int n = 0; n < FIELD_SIZE; n++)
                internalRepresentation[i][n] = FieldState.Empty;
        random = new Random();
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, getId());
        int middle = FIELD_SIZE / 2;
        if (internalRepresentation[middle][middle] == FieldState.Empty) {
            content.put(Keys.X, "" + middle);
            content.put(Keys.Y, "" + middle);
        } else {
            var myValue = FieldCalculator.calculateFieldValue(internalRepresentation, FieldState.PlayerA);
            var opValue = FieldCalculator.calculateFieldValue(internalRepresentation, FieldState.PlayerB);
            List<int[]> possibleMoves = new ArrayList<>();// X,Y,Own Value, Opponent Value
            for (int x = 0; x < FIELD_SIZE; x++) {
                for (int y = 0; y < FIELD_SIZE; y++) {
                    if (internalRepresentation[x][y] != FieldState.Empty)
                        continue;
                    var tempField = getCopyOfField();
                    tempField[x][y] = FieldState.PlayerA;
                    possibleMoves
                            .add(new int[] { x, y, FieldCalculator.calculateFieldValue(tempField, FieldState.PlayerA),
                                    FieldCalculator.calculateFieldValue(tempField, FieldState.PlayerB) });
                }
            }
            System.out.println(String.format("Own: %d and oponent: %d", myValue, opValue));
            for (int[] element : possibleMoves)
                System.out.println(
                        String.format("X=%d Y=%d my=%d; op=%d", element[0], element[1], element[2], element[3]));

                        var min = possibleMoves.stream().min((a, b) -> {
                return a[3] - b[3];
            }).get();
            System.out.println(String.format("MIN:\tX=%d Y=%d my=%d; op=%d", min[0], min[1], min[2], min[3]));
            var filtered = possibleMoves.stream().filter(x -> {
                return x[3] == min[3];
            }).collect(Collectors.toList());

            var max = filtered.stream().max((a, b) -> {
                return a[2] - b[2];
            }).get();
            System.out.println(String.format("MAX:\tX=%d Y=%d my=%d; op=%d", max[0], max[1], max[2], max[3]));
            
            filtered = possibleMoves.stream().filter(x -> {
             return x[2] == max[2];
             }).collect(Collectors.toList());
            
            var choise = filtered.get(random.nextInt(filtered.size()));
            content.put(Keys.X, "" + choise[0]);
            content.put(Keys.Y, "" + choise[1]);
        }
        com.sendMessage(Message.Set, content);
    }

    private void set(Map<Keys, String> content) {
        var id = content.get(Keys.Player).equals(gameID) ? FieldState.PlayerA : FieldState.PlayerB;
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

    private FieldState[][] getCopyOfField() {
        FieldState[][] returnValue = new FieldState[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++)
            for (int n = 0; n < FIELD_SIZE; n++)
                returnValue[i][n] = internalRepresentation[i][n];
        return returnValue;
    }
}
