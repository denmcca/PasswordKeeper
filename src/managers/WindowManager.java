package managers;

import components.SceneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.Logger;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class WindowManager extends Application {
    private Stage _stage;
    private int _width, _height;
//    private float ratio = 300f/480f; //TODO: allow window to adjust to size of screen
    private Path path = FileSystems.getDefault().getPath("images","lock-outline.png");

    public WindowManager(){
        Logger.debug(this, "constructor");
//        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
//        _width = screenWidth / 2; // default size
//        _height = (int)(_width * ratio); // default size
        _width = 480;
        _height = 300;
        Logger.debug(this, "width: " + _width + ", height: " + _height);
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

        // custom window icon for application
        setIcon("file:" + path.toString());

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

    public void setIcon(String _iconPath){
        _stage.getIcons().add(new Image(_iconPath));
    }
}
