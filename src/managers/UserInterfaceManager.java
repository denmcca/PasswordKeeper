package managers;

import utils.Logger;
import components.SceneBuilder;

public class UserInterfaceManager {
	private static UserInterfaceManager instance;
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
		if (instance == null) {
			instance = new UserInterfaceManager();
		} return instance;
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
