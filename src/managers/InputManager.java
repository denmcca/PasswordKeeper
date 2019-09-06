package managers;

import java.util.Scanner;

public class InputManager {
	private static InputManager _inputManager = null;
	private Scanner _scanner;
	
	private InputManager() {
		_scanner = new Scanner(System.in);
	};
	
	public static InputManager getInstance() {
		if (_inputManager == null) {
			_inputManager = new InputManager();
		}
		return _inputManager;
	}
	
	public static void pressEnter() {
		getInstance()._scanner.nextLine();
	}
	
	public Scanner scanner() {
		return _scanner;
	}
	
	public void clearScanner() { // doesn't work
		while(_scanner.hasNextLine()) {
			_scanner.nextLine();
		};
	}
}
