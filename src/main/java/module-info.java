module MyTicTacAI2 {
    requires javafx.controls;
    requires javafx.fxml;
requires transitive javafx.graphics;

    opens MyTicTacAI2 to javafx.fxml;
    exports MyTicTacAI2;
}