package MyTicTacAI2;

import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import MyTicTacAI2.Interfaces.IObserver;
import MyTicTacAI2.Interfaces.IPlayer;
import MyTicTacAI2.Player.Human;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.cell.CheckBoxListCell;

public class PrimaryController implements Initializable {

    @FXML
    private ComboBox<IPlayer> PlayerASelector;
    @FXML
    private ComboBox<IPlayer> PlayerBSelector;
    @FXML
    private Spinner<Integer> NumberOfGames;
    @FXML
    private ListView<CheckBoxListCell<IObserver>> ObserverList;
    @FXML
    private Button startStop;
    @FXML
    private Label stateDisplay;
    private boolean running;

    public PrimaryController() {
        running = false;
    }

    @FXML
    private void toggleRunningState() throws IOException {
        running = !running;
        setObjectsEnabled();
    }

    private void setObjectsEnabled() {
        startStop.setText(running ? "Stop" : "Start");
        PlayerASelector.setDisable(running);
        PlayerBSelector.setDisable(running);
        NumberOfGames.setDisable(running);
        ObserverList.setDisable(running);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        PlayerASelector.getItems().add(new Human());
    }
}
