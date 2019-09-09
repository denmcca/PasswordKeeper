package testing;

import managers.*;
import org.junit.jupiter.api.Test;
import utils.Logger;

class UserInterfaceTest {

	
	@Test
	void test_launch_window() {
		Logger.debug(this, "test_launch_window");
		UserInterfaceManager uim = UserInterfaceManager.getInstance();
//		uim.launchCreatePassword();
//		InputManager.pressEnter();
	}

	@Test
	void test_launch_window_password_manager() {

		Logger.debug(this, "test_launch_window_password_manager");
		EncryptionManager.getInstance();
		DataManager.getInstance();
		PasswordManager.getInstance();
		FileManager.getInstance();
		InputManager.getInstance();
		KeyManager.getInstance().load();
		LoginManager.getInstance();
		UserInterfaceManager uim = UserInterfaceManager.getInstance();
//		uim.launchPasswordManager();
//		InputManager.pressEnter();
	}


}