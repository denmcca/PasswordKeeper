package application.gui.components;

import application.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManager extends Application {
    private Stage stage;
    private int width, height;

    public WindowManager(){
        Logger.debug(this, "constructor");
        width = 400; // default size
        height = 300; // default size
    }

    public void go(){
        Logger.debug(this, "go");
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Logger.debug(this, "start");
        stage = primaryStage;
    }

    public void setScene(Scene scene) {
        Logger.debug(this, "setScene");
        stage.setScene(scene);
    }

    public void show(){
        Logger.debug(this, "show");
        stage.show();
    }

    public void hide(){
        Logger.debug(this, "hide");
        stage.hide();
    }

    public void close(){
        Logger.debug(this, "close");
        stage.close();
    }

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }
}
