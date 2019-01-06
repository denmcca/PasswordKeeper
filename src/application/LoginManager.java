package application;

public class LoginManager {
	private static LoginManager _loginManager = null;
	private byte[] _pHash;
	
	private LoginManager() {
		Logger.debug(this, "constructor");
	}
	
	public static LoginManager getInstance() {
		if (_loginManager == null) {
			_loginManager = new LoginManager();
		}
		return _loginManager;
	}
	
	public void init() {
//		_pHash = getPasswordHash();
		
	}
	
	private void createPasswordHash() {
		Logger.debug(this, "createPasswordHash");
		Logger.log("No hashed file found. Must create new hash!");
		Logger.log("Passwords will no longer be accessible!");
		// prompt user to continue
		// if no
			// return
		// if yes
			// prompt user to enter password
			// prompt user to enter password again
			// if password matches
				// prompt user for hint
					// store hint
			// hash password
			// store hash
	}
	
	public void sendPasswordToKeyManager(byte[] password) throws Exception {
		Logger.debug(this, "sendPasswordToKeyManager");
		KeyManager km = KeyManager.getInstance();
		km.receiveUserPassword(password);
	}
	
//	private byte[] getPasswordHash() {
//		// if hash file does not exist
//			// createPasswordHash()
//		
//		return PasswordManager.getInstance().getPasswordHash();
//	}
	
	private void createHint() {
		
	}
	
	private void getHint() {
		
	}
}
