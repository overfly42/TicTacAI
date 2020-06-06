module MyTicTacAI2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens MyTicTacAI2 to javafx.fxml;
    opens MyTicTacAI2.UI to javafx.fxml;

    exports MyTicTacAI2;
    exports MyTicTacAI2.Game;
    exports MyTicTacAI2.Interfaces;
    // exports MyTicTacAI2.Player;
    exports MyTicTacAI2.UI;
    exports MyTicTacAI2.utils;
}