import utils.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import managers.UserInterfaceManager;

public class Main extends Application {
	public static void main(String[] args) {
		Logger.log("Begin application");
//		WindowManager wm = new WindowManager();
//		wm.go();
		try {
			UserInterfaceManager.getInstance().init();
		} catch (Exception e) {
			e.printStackTrace();
		}

//		PKApplication app = new PKApplication();
//		app.start();
	}

	@Override
	public void start(Stage primaryStage) {
//		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
//	public static void main(String[] args) {
//		launch(args);
//	}
}
