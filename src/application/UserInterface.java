package application;

import java.awt.Label;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UserInterface {
	public Button button;
	public Scene scene;
	
	public UserInterface() {
		Logger.debug(this, "constructor");
	}
	
	public byte[] getPassword() {
		Logger.debug(this, "getPassword");
		String input = null;
		while (input == null) {
			System.out.print("Enter password: ");
			input = InputManager.getInstance().scanner().nextLine();
		}
		return input.getBytes();
	}

	public void createPassword(){
//		Password
	}
}
