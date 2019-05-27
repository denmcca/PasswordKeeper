package application.gui.components;

import application.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManager extends Application {
    private Stage _stage;
    private int _width, _height;

    public WindowManager(){
        Logger.debug(this, "constructor");
        _width = 400; // default size
        _height = 300; // default size
    }

    public void go(){
        Logger.debug(this, "go");
        launch();
        Logger.debug(this, "Exit go");
    }

    @Override
    public void start(Stage primaryStage) throws Exception { //start is a dead end!!!
        Logger.debug(this, "start");
        _stage = primaryStage;
//        _stage.setResizable(false);
        // Makes Window closable using X button in title bar
        _stage.setOnCloseRequest(we -> exit());

        setSizeProperties();

        SceneBuilder.getInstance().init(this);

        // if password hash file not exist do not load data
        if (LoginManager.getInstance().isFirstTimeLogin()){
            SceneBuilder.getInstance().postponeDataLoad(true);
        }
        UserInterfaceManager.getInstance().setWindowManager(this);
        SceneBuilder.getInstance().addLayer(SceneBuilder
                .getInstance().buildPasswordManagerLayer()); // trying to access data file before able to decrypt!

        SceneBuilder.getInstance().createScene();
        setScene(SceneBuilder.getInstance().getScene());

        if (LoginManager.getInstance().isFirstTimeLogin()) {
            SceneBuilder.getInstance().addLayer(SceneBuilder
                    .getInstance().buildCreatePasswordLayer());
        } else {
            SceneBuilder.getInstance().addLayer(SceneBuilder
                    .getInstance().buildLoginLayer());
        }

        show();
    }

    public Stage getStage(){
        return _stage;
    }

    private void setSizeProperties(){
        _stage.setMinWidth(_width);
        _stage.setMinHeight(_height);
        _stage.setWidth(_width);
        _stage.setHeight(_height);
    }

    private void setScene(Scene scene) {
        Logger.debug(this, "setScene");
        _stage.setScene(scene);
    }

    public void show(){
        Logger.debug(this, "show");
        _stage.show();
    }

    public void close(){
        Logger.debug(this, "close");
        _stage.close();
    }

    public void exit(){
        close();
        System.exit(0);
    }

    public void setTitle(String title) {
        _stage.setTitle(title);
    }
}
