import utils.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import managers.UserInterfaceManager;

import javax.swing.*;

public class Main extends Application {
	public static void main(String[] args) {
		Logger.log("Begin application");
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName()); // doesn't seem to work!
		}
		catch (UnsupportedLookAndFeelException |
				ClassNotFoundException |
				InstantiationException |
				IllegalAccessException e) {
			e.printStackTrace();
		}

		UserInterfaceManager.getInstance().init();
	}

	@Override
	public void start(Stage primaryStage) {}
	
}
