package components;

import interfaces.PopUpWindow;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import utils.Logger;

/*
    Used to display messages to user. No inputs other than a confirmation button.
*/
public class NotificationWindow extends PopUpWindow {
    // enums for each type
    public enum WindowType {
        CONFIRM
    }

    public static void launchWindow(String title, String messageIn, WindowType type) {
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        anchorPane = new AnchorPane();
        message = messageIn;
        switch (type) {
            case CONFIRM:
                confirmationNotification();
                break;
            default:
                Logger.log("No valid window type found in Notification Window");
        }
        window.showAndWait();
        window.close();
    }
    private static void confirmationNotification() {
        Label messageLabel = new Label(message);
        Button confirmButton = new Button("OK");


    }
}
