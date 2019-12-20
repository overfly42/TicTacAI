module MyTicTacAI2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter;

    opens MyTicTacAI2 to javafx.fxml;
    exports MyTicTacAI2;
}