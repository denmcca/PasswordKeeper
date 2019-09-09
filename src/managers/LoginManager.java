package managers;

import utils.Logger;

import java.io.IOException;

public class LoginManager {
	private static LoginManager instance = null;
	private PasswordManager passwordManager;
	private boolean isLoggedIn;
	
	private LoginManager() {
		Logger.debug(this, "constructor");
		isLoggedIn = false;
		passwordManager = PasswordManager.getInstance();
	}

	/**
	 * Singleton
	 */
	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}
	
	public void init() {
//		_pHash = getPasswordHash();
		
	}

	/**
	 * Sets user as logged in, and sends password to key manager.
	 * */
	private void login(byte[] pass) {
		isLoggedIn = true;
		sendPasswordManagerPassword(pass);
	}

	/**
	 * Sends password password manager to verify user entered password during login.
	 * Returns true if user was logged in.
	 * */
	public boolean attemptLogin(byte[] pass) {
		if (isPasswordCorrect(pass)) {
			Logger.debug(this, "password was correct");
			login(pass);
			return true;
		}
		Logger.debug(this, "password was incorrect");
		return false;
	}

	/**
	 * Accesses Password Manager to check if given password is correct.
	 * Returns true if password is correct.
	 * */
	private boolean isPasswordCorrect(byte[] pass) {
		return passwordManager.verifyPassword(pass);
	}

	/**
	 * Sends password to Password Manager to create password hash.
	 * */
	public void setPasswordHash(byte[] password) {
		Logger.debug(this, "createPasswordHash");
		passwordManager.setPasswordHash(password);
	}

	/**
	 * Sends user entered password to Password Manager.
	 * Note: Password Manager does not keep copy of password, instead sends it to Key Manager.
	 * */
	public void sendPasswordManagerPassword(byte[] password) {
		Logger.debug(this, "sendPasswordToKeyManager");
		passwordManager.receiveUserPassword(password);
	}

	/**
	 * Checks if hash file exists
	 * Returns true if exists.
	 * */
	public boolean isFirstTimeLogin() {
		Logger.debug(this, "isFirstTimeLogin");
		Logger.debug(this, "Password hash exists: "
				+ PasswordManager.getInstance().passwordHashFileExists().toString());
		return !PasswordManager.getInstance().passwordHashFileExists();
	}

	/**
	 * Loads password hash from file
	 * */
	public void loadPasswordHashFile() {
		PasswordManager.getInstance().load();
	}

	public void savePasswordHashFile() {
		Logger.debug(this, "savePasswordHashFile");
		PasswordManager.getInstance().save();
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}
}
