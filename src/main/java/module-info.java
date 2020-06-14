module MyTicTacAI2 {
    requires com.rabbitmq.client;
    requires org.slf4j;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;

    opens MyTicTacAI2 to javafx.fxml;
    opens MyTicTacAI2.UI to javafx.fxml;
//    opens org.slf4j to com.rabbitmq.client;


    exports MyTicTacAI2;
    exports MyTicTacAI2.Game;
    exports MyTicTacAI2.Interfaces;
    // exports MyTicTacAI2.Player;
    exports MyTicTacAI2.UI;
    exports MyTicTacAI2.utils;
    exports MyTicTacAI2.Communication;
}