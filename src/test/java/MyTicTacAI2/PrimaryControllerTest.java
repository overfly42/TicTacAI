package MyTicTacAI2;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import MyTicTacAI2.UI.PrimaryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class PrimaryControllerTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(PrimaryController.class.getResource("primary.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }
    @Before
    public void setUp() throws Exception
    {
    }
    @After
    public void tearDown() throws Exception
    {
       FxToolkit.hideStage();
       release(new KeyCode[]{});
       release(new MouseButton[]{});
    }
    @Test
    public void testtest()
    {
        clickOn("#startStop");
        assertTrue(true);
    }
}