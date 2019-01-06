package application.gui.components;

import application.Logger;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneManager {
    private Scene _scene;
    private int _width, height;
    private Pane layout;

    public SceneManager() {
        Logger.debug(this, "constructor");
        _width = 400; // default _width
        height = 300; // default height
    }

    public void set_width(int _width){
        Logger.debug(this, "set_width");
        this._width = _width;
    }

    public void setHeight(int height){
        Logger.debug(this, "setHeight");
        this.height = height;
    }

    public void setLayout(Pane layout){
        Logger.debug(this, "setLayout");
        this.layout = layout;
    }

    public Scene createScene(){
        Logger.debug(this, "createScene");
        if (layout == null) Logger.log("layout is null in SceneManager\n" +
                "cannot create _scene yet!");
        else _scene = new Scene(layout, _width, height);
        return get_scene();
    }

    public Scene get_scene(){
        Logger.debug(this , "get_scene");
        return _scene;
    }

    public int get_width(){
        Logger.debug(this , "get_width");
        return _width;
    }

    public int getHeight(){
        Logger.debug(this , "getHeight");
        return height;
    }

    public void addChild(Node child){
        Logger.debug(this , "addChild");

        layout.getChildren().add(child);
    }

    public void addChildren(Node...children){
        Logger.debug(this , "addChildren");
        layout.getChildren().addAll(children);
    }
}
