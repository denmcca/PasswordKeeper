package interfaces;

import components.NotificationWindow;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public abstract class PopUpWindow {
    protected static Stage window;
    protected static String message;
    protected static AnchorPane anchorPane;

    protected PopUpWindow() {
        window = new Stage();
    }
}
