package application.gui;

import application.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.*;

public class PasswordCreator extends Application {

    public void go(){
        Logger.debug(this, "go");
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Logger.debug(this, "start");
        TextField pass1 = new TextField("Enter Password");
        TextField pass2 = new TextField("Re-enter Password");
        Label messageError = new Label("");
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");

        primaryStage.setTitle("Create Password");

        buttonSave.setMnemonicParsing(true);

        buttonSave.setOnAction(value -> {
//            buttonSave.setMnemonicParsing(true);
//            if (!buttonSave.getText().equals("_clicked!"))
//                buttonSave.setText("_clicked!");
//            else buttonSave.setText("un_clicked!");
            Logger.debug(this, pass1.getText());
            if (compareCreatedPassword(pass1.getText(), pass2.getText())) {
                messageError.setText("Password successfully created.");
                submitPassword();
            } else messageError.setText("Passwords did not match. Try again or cancel.");
        });

        buttonCancel.setOnAction(value -> {
            primaryStage.close();
            System.exit(0);
        });

//        StackPane layout = new StackPane();
        VBox layout = new VBox(10);
        HBox layout2 = new HBox(10);
        VBox.setMargin(pass1, new Insets(10,10,10,10));
        VBox.setMargin(pass2, new Insets(10,10,10,10));
        HBox.setMargin(buttonSave, new Insets(10,10,10,10));
        HBox.setMargin(buttonCancel, new Insets(10,10,10,10));
        VBox.setMargin(messageError, new Insets(10,10,10,10));
        layout.getChildren().add(pass1);
        layout.getChildren().add(pass2);
        layout2.getChildren().add(buttonSave);
        layout2.getChildren().add(buttonCancel);
        layout.getChildren().add(layout2);
        layout.getChildren().add(messageError);




//		layout.getChildren().add(label);
//		Group root = new Group();
        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void submitPassword() {
        Logger.debug(this, "submitPassword");
        Logger.log("password created!");
    }

    private boolean compareCreatedPassword(String password1, String password2) {
        Logger.debug(this, "compareCreatedPassword");
        return (password1.equals(password2));
    }
}
