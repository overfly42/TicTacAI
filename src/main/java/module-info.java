module MyTicTacAI2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens MyTicTacAI2 to javafx.fxml;
    exports MyTicTacAI2;
}