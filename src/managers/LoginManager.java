package managers;

import utils.Logger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class LoginManager {
	private static LoginManager _loginManager = null;
	private KeyManager keym;
	private PasswordManager passm;
	private byte[] _pHash;
	
	private LoginManager() throws IOException, NoSuchAlgorithmException {

		Logger.debug(this, "constructor");
		keym = KeyManager.getInstance();
		passm = PasswordManager.getInstance();
	}
	
	public static LoginManager getInstance()
			throws IOException, NoSuchAlgorithmException {
		if (_loginManager == null) {
			_loginManager = new LoginManager();
		}
		return _loginManager;
	}
	
	public void init() {
//		_pHash = getPasswordHash();
		
	}
	
	public void createAssignPasswordHash(byte[] password)
			throws InvalidKeySpecException, NoSuchAlgorithmException,
			IOException {
		Logger.debug(this, "createPasswordHash");
		passm.setPasswordHash(passm.generatePasswordHash(password, keym.getSalt()));
	}
	
	public void sendPasswordToKeyManager(byte[] password) throws InvalidKeySpecException, NoSuchAlgorithmException {
		Logger.debug(this, "sendPasswordToKeyManager");
		keym.receiveUserPassword(password);
	}

	// Checks if hash file exists
	public boolean isFirstTimeLogin() throws IOException {
		Logger.debug(this, "isFirstTimeLogin");
		Logger.debug(this, "Password hash exists: "
				+ PasswordManager.getInstance().passwordHashFileExists().toString());
		return !PasswordManager.getInstance().passwordHashFileExists();
	}

	// Loads password hash from file
	public void loadPasswordHashFile() throws IOException {
		PasswordManager.getInstance().load();
	}

	public void savePasswordHashFile() throws IOException {
		Logger.debug(this, "savePasswordHashFile");
		PasswordManager.getInstance().save();
	}


	
//	private byte[] getPasswordHash() {
//		// if hash file does not exist
//			// createPasswordHash()
//		
//		return PasswordManager.getInstance().getPasswordHash();
//	}

	// Public interface hashes given password and compares with hash file.
	public boolean doesPasswordMatch(String password) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		Logger.debug(this, "doesPasswordMatch");
		byte[] passHash = passm.generatePasswordHash(password.getBytes(), keym.getSalt());
		return PasswordManager.getInstance().verifyPassword(passHash);
	}
	
	private void createHint() {
		
	}
	
	private void getHint() {
		
	}
}
