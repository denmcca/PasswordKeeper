package application;

import application.gui.components.SceneBuilder;
import application.gui.components.WindowManager;

public class UserInterfaceManager {
	private static UserInterfaceManager _instance;
	private WindowManager _windowManager;

/**
 * Should be responsible for preparing GUI to work with rest of system
 * LoginManager should be API on backend, and this should act as API for GUI/Frontend
*/

	private UserInterfaceManager() {
		Logger.debug(this, "constructor");
		_windowManager = new WindowManager();
	}

	public static UserInterfaceManager getInstance() {
		if (_instance == null) {
			_instance = new UserInterfaceManager();
		} return _instance;
	}

	public void init() {
		_windowManager.go(); // set up stage
	}

	public void launchCreatePassword() {
		Logger.debug(this, "launchCreatePassword");
		SceneBuilder.getInstance().addLayer(SceneBuilder
				.getInstance().buildPasswordManagerLayer());
	}

	public void launchLogin(){
		Logger.debug(this, "launchLogin");
		SceneBuilder.getInstance().addLayer(SceneBuilder
				.getInstance().buildLoginLayer());

	}

	public void launchPasswordManager() {
		Logger.debug(this, "launchPasswordManager");
	}


	public void loadData() throws Exception {
		DataManager.getInstance().createFileIfNotExist();
		DataManager.getInstance().loadData();
	}

	public WindowManager getWindowManager() {
		return _windowManager;
	}

	public void setWindowManager(WindowManager windowManager) {
		_windowManager = windowManager;
	}
}
