package MyTicTacAI2.UI;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import MyTicTacAI2.Game.SingleGameController;
import MyTicTacAI2.Game.GameExecutionState;
import MyTicTacAI2.Game.MultiGameController;
import MyTicTacAI2.Interfaces.IGameController;
import MyTicTacAI2.Interfaces.IGameStateObserver;
import MyTicTacAI2.Interfaces.IObserver;
import MyTicTacAI2.Interfaces.IPlayer;
import MyTicTacAI2.Player.Human;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

public class PrimaryController implements Initializable, IGameStateObserver {

    @FXML
    private ComboBox<String> PlayerASelector;
    @FXML
    private ComboBox<String> PlayerBSelector;
    @FXML
    private ComboBox<String> startPlayer;
    @FXML
    private Spinner<Integer> NumberOfGames;
    @FXML
    private ListView<IObserver> ObserverList;
    @FXML
    private Button startStop;
    @FXML
    private Label stateDisplay;
    private boolean running;
    private List<Class<? extends IObserver>> gameOberserver;
    private List<IObserver> activeObservers;
    private Map<String, Class<? extends IPlayer>> potentialPlayer;
    private MultiGameController gameController;

    public PrimaryController() {
        running = false;
        potentialPlayer = new HashMap<>();
        gameOberserver = new ArrayList<>();
        activeObservers = new ArrayList<>();
        potentialPlayer.put("Human", Human.class);
        gameOberserver.add(Human.class);
        gameController = new MultiGameController();
    }

    @FXML
    private void toggleRunningState() throws IOException {
        running = !running;
        setObjectsEnabled();
        // gameController.addGameStateObserver(this);
        activeObservers.clear();
        if (running) {
            for (var x : ObserverList.getItems()) {

            }
            startNewGame();
        }
    }

    private void setObjectsEnabled() {
        startStop.setText(running ? "Stop" : "Start");
        PlayerASelector.setDisable(running);
        PlayerBSelector.setDisable(running);
        NumberOfGames.setDisable(running);
        ObserverList.setDisable(running);
        startPlayer.setDisable(running);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        PlayerASelector.getItems().addAll(potentialPlayer.keySet());
        PlayerBSelector.getItems().addAll(potentialPlayer.keySet());
        // for (Class<? extends IPlayer> player : potentialPlayer) {
        // try {
        // PlayerASelector.getItems().add(player.getDeclaredConstructor().newInstance());
        // PlayerBSelector.getItems().add(player.getDeclaredConstructor().newInstance());
        // } catch (InstantiationException | IllegalAccessException |
        // IllegalArgumentException
        // | InvocationTargetException | NoSuchMethodException | SecurityException e) {
        // (new Alert(AlertType.ERROR, "Could not add Player Type " +
        // player.toString())).showAndWait();
        // e.printStackTrace();
        // }
        // }

        ObserverList.setCellFactory(CheckBoxListCell.forListView(new Callback<IObserver, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(IObserver param) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    System.out.println(obs + " " + wasSelected + "  was " + isNowSelected);
                });
                return observable;
            }

        }));

        ObserverList.getItems().add(new Human());
        PlayerASelector.setValue(PlayerASelector.getItems().get(0));
        PlayerBSelector.setValue(PlayerBSelector.getItems().get(0));
        // for(Class<? extends IObserver> observer:gameOberserver)
        // {
        // CheckBoxListCell<String> box = new CheckBoxListCell<>();

        // box.setDisable(false);
        // ObserverList.getItems().add(box);
        //
        startPlayer.getItems().add("Spieler A");
        startPlayer.getItems().add("Spieler B");
        startPlayer.setValue("SpielerA");

        IntegerSpinnerValueFactory spinnerInit = new IntegerSpinnerValueFactory(1, 100, 1);
        NumberOfGames.setValueFactory(spinnerInit);
    }

    @Override
    public void switchedGameStateTo(GameExecutionState newState) {
        if (newState == GameExecutionState.Finish)
            startNewGame();
    }

    private void startNewGame() {
        gameController.startGame(potentialPlayer.get(PlayerASelector.getValue()),
         potentialPlayer.get(PlayerBSelector.getValue()),
          null,
          NumberOfGames.getValue().intValue());
    }
}
