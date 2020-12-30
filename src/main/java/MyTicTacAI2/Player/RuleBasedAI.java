package MyTicTacAI2.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.utils.FieldCalculator;

public class RuleBasedAI extends Player {

    public static final String BASE_ID = "SIMPLE_AI";
    private final String gameID;
    private final static int FIELD_SIZE = 3;
    private FieldState internalRepresentation[][] = new FieldState[FIELD_SIZE][FIELD_SIZE];
    private java.util.Random random;
    private Message LastMessage;

    public RuleBasedAI(String id) {
        isActive = false;
        gameID = BASE_ID + "_" + id;
        resetField(false);
        random = new Random();
        LastMessage = Message.StartGame;
    }

    private synchronized void resetField() {
        resetField(true);
    }

    private synchronized void resetField(boolean output) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (output)
                System.out.print(gameID + "\t");
            for (int n = 0; n < FIELD_SIZE; n++) {
                if (output) {

                    String fieldValue = " ";
                    switch (internalRepresentation[i][n]) {
                        case Empty:
                            fieldValue = "-";
                            break;
                        case PlayerA:
                            fieldValue = "x";
                            break;
                        case PlayerB:
                            fieldValue = "o";
                            break;
                    }
                    System.out.print(fieldValue + "\t");
                }
                internalRepresentation[i][n] = FieldState.Empty;
            }
            System.out.println();
        }

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
                LastMessage = Message.StartGame;
                break;
            case Set:
                set(content);
                LastMessage = Message.Set;
                break;
            case Turn:
                turn();
                LastMessage = Message.Turn;
                break;
            case EndGame:
                resetField();
                break;
            case SetRejected:
                if (content.get(Keys.Reason).equals("Field not empty"))
                    turn();
                else
                    System.out.println("(" + gameID + ") Sorry not my Turn");
                break;
            default:
                System.out.println("Recevied not implemented message: " + msg);
                for (Keys k : content.keySet())
                    System.out.println("\t" + k + ":\t" + content.get(k));
        }
    }

    private void turn() {
        int timeout = 10;
        try {
            while (timeout-- > 0 && LastMessage != Message.Set)
                Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, getId());
        int middle = FIELD_SIZE / 2;
        // if (internalRepresentation[middle][middle] == FieldState.Empty) {
        // //If the middle position is free, take it as it is the
        // content.put(Keys.X, "" + middle);
        // content.put(Keys.Y, "" + middle);
        // } else {
        // Collect the current state of the field
        var myValue = FieldCalculator.calculateFieldValue(internalRepresentation, FieldState.PlayerA);
        var opValue = FieldCalculator.calculateFieldValue(internalRepresentation, FieldState.PlayerB);

        // Collect all allowed moves
        List<int[]> possibleMoves = new ArrayList<>();// X,Y,Own Value, Opponent Value
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                if (internalRepresentation[x][y] != FieldState.Empty)
                    continue;
                var tempField = getCopyOfField();
                tempField[x][y] = FieldState.PlayerA;
                var my = FieldCalculator.calculateFieldValue(tempField, FieldState.PlayerA);
                var op = FieldCalculator.calculateFieldValue(tempField, FieldState.PlayerB);
                var delta = Math.abs(myValue - my) + Math.abs(opValue - op);
                possibleMoves.add(new int[] { x, y, my, op, delta });
            }
        }
        System.out.println(String.format("Own: %d and oponent: %d", myValue, opValue));
        for (int[] element : possibleMoves)
            System.out.println(String.format("X=%d Y=%d my=%d; op=%d", element[0], element[1], element[2], element[3]));
        if (possibleMoves.isEmpty()) {
            System.out.println("NO More moves possible...");
            return;
        }
        int selectionMode = 2;
        int[] choise = new int[5];
        if (selectionMode == 1) {
            // find out, what is the worst for the oponent
            var min = possibleMoves.stream().min((a, b) -> {
                return a[3] - b[3];
            }).get();
            System.out.println(String.format("MIN:\tX=%d Y=%d my=%d; op=%d", min[0], min[1], min[2], min[3]));
            var filtered = possibleMoves.stream().filter(x -> {
                return x[3] == min[3];
            }).collect(Collectors.toList());

            // find out, what of the worst for the oponent ist best for AI
            var max = filtered.stream().max((a, b) -> {
                return a[2] - b[2];
            }).get();
            System.out.println(String.format("MAX:\tX=%d Y=%d my=%d; op=%d", max[0], max[1], max[2], max[3]));

            filtered = possibleMoves.stream().filter(x -> {
                return x[2] == max[2];
            }).collect(Collectors.toList());

            // do move, randomly if more than 1 possiblity is given
            System.out.println(String.format("AI Have to choose between %d posibilitys", filtered.size()));

            choise = filtered.get(random.nextInt(filtered.size()));
        }
        if (selectionMode == 2) {
            var maxDelta = possibleMoves.stream().max((a, b) -> {
                return a[4] - b[4];
            }).get()[4];
            var filtered = possibleMoves.stream().filter(x -> {
                return x[4] == maxDelta;
            }).collect(Collectors.toList());
            System.out.println(String.format("AI Have to choose between %d posibilitys", filtered.size()));

            choise = filtered.get(random.nextInt(filtered.size()));
        }
        content.put(Keys.X, "" + choise[0]);
        content.put(Keys.Y, "" + choise[1]);
        // }
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
