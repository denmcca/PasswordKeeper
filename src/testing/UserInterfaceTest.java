package testing;

import static org.junit.jupiter.api.Assertions.*;

import application.gui.PasswordCreator;
import org.junit.jupiter.api.Test;

import application.InputManager;
import application.Logger;
import application.UserInterface;

class UserInterfaceTest {

	
	@Test
	void test_launch_window() {
		Logger.debug(this, "test_launch_window");
		PasswordCreator ui = new PasswordCreator();
		ui.go();
		InputManager.pressEnter();
	}
}