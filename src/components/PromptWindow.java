package components;

import interfaces.PopUpWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Logger;

public class PromptWindow extends PopUpWindow {
    private static boolean answer;

    private PromptWindow() {
        super();
    }
    public enum WindowType {
        YES_NO
    }

    public static boolean launchWindow(String title, String messageIn, WindowType type) {
        window = new Stage();
        window.setTitle(title);
        message = messageIn;

        window.initModality(Modality.APPLICATION_MODAL);

        anchorPane = new AnchorPane();

        switch (type) {
            case YES_NO:
                PromptWindow.yesNoPrompt();
                break;
            default:
                Logger.log("No valid Window Type found in Prompt Window");
        }

        window.showAndWait();
        window.close();
        return answer;
    }

    private static void yesNoPrompt() {
        Label questionLabel = new Label(message);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        yesButton.setDefaultButton(true);

        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        noButton.cancelButtonProperty();

        HBox labelBox = new HBox(questionLabel);
        labelBox.setAlignment(Pos.TOP_CENTER);
        HBox.setMargin(questionLabel, new Insets(5f));

        HBox buttonBox = new HBox(5f, yesButton, noButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        HBox.setMargin(yesButton, new Insets(5f));
        HBox.setMargin(noButton, new Insets(5f));

        VBox layoutBox = new VBox(labelBox, buttonBox);
        anchorPane.getChildren().addAll(layoutBox);

        Scene scene = new Scene(anchorPane);
        window.setScene(scene);
    }
}
