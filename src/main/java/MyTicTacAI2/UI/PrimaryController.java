package MyTicTacAI2.UI;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import MyTicTacAI2.Communication.ServerQueue;
import MyTicTacAI2.Game.GameStateMachine;
import MyTicTacAI2.Interfaces.IComQueue;
import MyTicTacAI2.Player.HumanPlayer;
import MyTicTacAI2.Player.NonePlayer;
import MyTicTacAI2.Player.Player;
import MyTicTacAI2.Player.RuleBasedAI;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PrimaryController implements Initializable {

    @FXML
    private Spinner<Integer> NumberOfGames;
    @FXML
    private Button startStop;
    @FXML
    private Label stateDisplay;
    @FXML
    private Label L00;
    @FXML
    private Label L01;
    @FXML
    private Label L02;
    @FXML
    private Label L10;
    @FXML
    private Label L11;
    @FXML
    private Label L12;
    @FXML
    private Label L20;
    @FXML
    private Label L21;
    @FXML
    private Label L22;
    @FXML
    private TextField playerAName;
    @FXML
    private TextField playerBName;
    @FXML
    private Label currentPlayer;
    @FXML
    private TextArea history;
    @FXML
    private ComboBox<Player> playerTypeA;
    @FXML
    private ComboBox<Player> playerTypeB;
    private boolean running;
    private List<SimpleStringProperty> fieldView;
    private SimpleStringProperty playerAProperty;
    private SimpleStringProperty playerBProperty;
    private SimpleStringProperty currentProperty;

    private GameStateMachine stateMachine;
    private IComQueue queue;

    private Player backendP1;
    private Player backendP2;

    public PrimaryController() {
        running = false;
        fieldView = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            fieldView.add(new SimpleStringProperty());
        playerAProperty = new SimpleStringProperty();
        playerBProperty = new SimpleStringProperty();
        currentProperty = new SimpleStringProperty();
        queue = new ServerQueue("server_in");
        stateMachine = new GameStateMachine(queue);

    }

    @FXML
    private void toggleRunningState() {
        running = !running;
        setObjectsEnabled();
        if (stateMachine.isActivated()) {
            stateMachine.stopStateMaschine();
            backendP1.stop();
            backendP2.stop();
            for (SimpleStringProperty text : fieldView)
                text.set("");
            history.textProperty().set("");
        } else {
            backendP1.start();
            backendP2.start();
            stateMachine.startStateMaschine();
            stateMachine.getBoard().setMaxGames(NumberOfGames.getValue());

        }
    }

    private void setObjectsEnabled() {
        startStop.setText(running ? "Stop" : "Start");
        NumberOfGames.setDisable(running);
        playerAName.setDisable(running);
        playerBName.setDisable(running);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        IntegerSpinnerValueFactory spinnerInit = new IntegerSpinnerValueFactory(1, 100, 1);
        NumberOfGames.setValueFactory(spinnerInit);
        initGameField();
        playerAName.textProperty().bindBidirectional(playerAProperty);
        playerBName.textProperty().bindBidirectional(playerBProperty);
        currentPlayer.textProperty().bind(currentProperty);
        history.textProperty().addListener((obs, oldVal, newVal) -> {
            history.setScrollTop(Double.MAX_VALUE);
        });
        initPlayerTypes();

    }

    private void initPlayerTypes() {
        Function<String, String> textAppender = e -> {
            history.appendText(e);
            return e;
        };
        backendP1 = new HumanPlayer(fieldView, playerAProperty, textAppender);
        backendP2 = new HumanPlayer(fieldView, playerBProperty, textAppender);

        initSingleComboBox(playerTypeA, backendP1, "A", (var x) -> {
            backendP1 = playerTypeA.getValue();
        });

        initSingleComboBox(playerTypeB, backendP2, "B", (var x) -> {
            backendP2 = playerTypeB.getValue();
        });
    }

    private void initSingleComboBox(ComboBox<Player> boxToFill, Player p, String id,
            EventHandler<ActionEvent> eventHandler) {
        boxToFill.getItems().add(p);
        boxToFill.getItems().add(new RuleBasedAI(id));
        boxToFill.getItems().add(new NonePlayer());

        boxToFill.setValue(boxToFill.getItems().get(0));
        boxToFill.setOnAction(eventHandler);
    }

    private void initGameField() {
        L00.textProperty().bind(fieldView.get(0));
        L01.textProperty().bind(fieldView.get(1));
        L02.textProperty().bind(fieldView.get(2));
        L10.textProperty().bind(fieldView.get(3));
        L11.textProperty().bind(fieldView.get(4));
        L12.textProperty().bind(fieldView.get(5));
        L20.textProperty().bind(fieldView.get(6));
        L21.textProperty().bind(fieldView.get(7));
        L22.textProperty().bind(fieldView.get(8));

        EventHandler<MouseEvent> clickHandler = (e) -> {
            char[] name = ((Label) e.getSource()).getId().toCharArray();
            int x = Integer.parseInt("" + name[1]);
            int y = Integer.parseInt("" + name[2]);
            if (backendP1.hasTurn() && !backendP2.hasTurn()) {
                backendP1.clickOn(x, y);
            } else if (!backendP1.hasTurn() && backendP2.hasTurn()) {
                backendP2.clickOn(x, y);
            } else {
                stateDisplay.setText("No Valid Turn");
            }
        };
        L00.setOnMouseClicked(clickHandler);
        L01.setOnMouseClicked(clickHandler);
        L02.setOnMouseClicked(clickHandler);
        L10.setOnMouseClicked(clickHandler);
        L11.setOnMouseClicked(clickHandler);
        L12.setOnMouseClicked(clickHandler);
        L20.setOnMouseClicked(clickHandler);
        L21.setOnMouseClicked(clickHandler);
        L22.setOnMouseClicked(clickHandler);
    }
}
