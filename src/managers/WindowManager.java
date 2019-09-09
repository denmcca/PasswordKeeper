package managers;

import components.PromptWindow;
import components.SceneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.Logger;

import java.awt.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class WindowManager extends Application {
    private Stage window;
    private int width, height;
    private float ratio = 300f/480f; //TODO: allow window to adjust to size of screen
    private Path path = FileSystems.getDefault().getPath("images","lock-outline.png");

    public WindowManager() {
        Logger.debug(this, "constructor");
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        width = screenWidth / 2; // default size
        height = (int)(width * ratio); // default size
        width = 480;
        height = 300;
        Logger.debug(this, "width: " + width + ", height: " + height);
    }

    public void go(){
        Logger.debug(this, "go");
        launch();
        Logger.debug(this, "Exit go");
    }

    @Override
    public void start(Stage primaryStage) { //start is a dead end!!!
        Logger.debug(this, "start");
        window = primaryStage;

        // Makes Window closable using X button in title bar
        window.setOnCloseRequest(we ->  {
            // Prevents window from closing to run code.
            we.consume();
            exit();
        });

        // custom window icon for application
        setIcon("file:" + path.toString());

        setSizeProperties();

        SceneBuilder.getInstance().init(this);

        // if password hash file not exist do not load data
        if (LoginManager.getInstance().isFirstTimeLogin()) {
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
        }
        else {
            SceneBuilder.getInstance().addLayer(SceneBuilder
                    .getInstance().buildLoginLayer());
        }
        show();
    }

    public Stage getStage(){
        return window;
    }

    private void setSizeProperties(){
        window.setMinWidth(width);
        window.setMinHeight(height);
        window.setWidth(width);
        window.setHeight(height);
    }

    private void setScene(Scene scene) {
        Logger.debug(this, "setScene");
        window.setScene(scene);
    }

    private void show(){
        Logger.debug(this, "show");
        window.show();
    }

    private void close(){
        Logger.debug(this, "close");
        window.close();
    }

    private void exit(){

        if (LoginManager.getInstance().isLoggedIn()) {
            boolean shouldSave = PromptWindow
                    .launchWindow("Save?",
                            "Would you like to save before exiting?",
                            PromptWindow.WindowType.YES_NO
                    );

            if (shouldSave) {
                try {
                    DataManager.getInstance().saveData();
                    Logger.log("Data saved!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        close();
        System.exit(0);
    }

    public void setTitle(String title) {
        window.setTitle(title);
    }

    private void setIcon(String _iconPath){
        window.getIcons().add(new Image(_iconPath));
    }
}
