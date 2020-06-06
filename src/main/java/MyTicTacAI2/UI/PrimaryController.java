package MyTicTacAI2.UI;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;


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
    private boolean running;
    private List<SimpleStringProperty> fieldView;

    public PrimaryController() {
        running = false;
        fieldView = new ArrayList<>();
        for(int i = 0; i < 9;i++)
            fieldView.add(new SimpleStringProperty());
    }

    @FXML
    private void toggleRunningState()  {
        running = !running;
        setObjectsEnabled();
    }

    private void setObjectsEnabled() {
        startStop.setText(running ? "Stop" : "Start");
        NumberOfGames.setDisable(running);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        IntegerSpinnerValueFactory spinnerInit = new IntegerSpinnerValueFactory(1, 100, 1);
        NumberOfGames.setValueFactory(spinnerInit);
        L00.textProperty().bind(fieldView.get(0));
        L01.textProperty().bind(fieldView.get(1));
        L02.textProperty().bind(fieldView.get(2));
        L10.textProperty().bind(fieldView.get(3));
        L11.textProperty().bind(fieldView.get(4));
        L12.textProperty().bind(fieldView.get(5));
        L20.textProperty().bind(fieldView.get(6));
        L21.textProperty().bind(fieldView.get(7));
        L22.textProperty().bind(fieldView.get(8));
    }
}
