package application;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordManager {
	
	private static PasswordManager _passwordManager = null;
	private byte[] _passwordHash = null;
	private int _iterations = 65536;
	private String _hashFileName = "hash.storage";

	
	private PasswordManager() {
		Logger.debug(this, "constructor");
	}
	
	public static PasswordManager getInstance() throws IOException {
		if (_passwordManager == null) {
			_passwordManager = new PasswordManager();
		}
		return _passwordManager;
	}
	
	public void load() throws IOException, NoSuchAlgorithmException {
		Logger.debug(this, "load");
		if (_passwordHash == null) {
			loadPasswordHash();
		}
	}
	
	public void save() throws Exception {
		Logger.debug(this, "save");
		if (_passwordHash != null) {
			savePasswordHash();
		}
	}
	
	// Load hash from file
	private void loadPasswordHash() throws IOException {
		Logger.debug(this, "loadPasswordHash");
		_passwordHash = FileManager.getInstance().readData(_hashFileName);
	}
	
	// Save hash to file
	private void savePasswordHash() throws IOException {
		Logger.debug(this, "savePasswordHash");
		FileManager.getInstance().writeData(_passwordHash, _hashFileName, false);
	}

	public void sendPasswordToKeyManager(byte[] password) throws Exception {
		Logger.debug(this, "sendPasswordToKeyManager");
		KeyManager.getInstance().receiveUserPassword(password);
	}
	
	// Generates hash. Hash generation also used to create hash at login.
	private byte[] generateSecret(byte[] password, byte[] salt) throws Exception { // salt as parameter to promote decoupling
		Logger.debug(this, "generateSecret");
		char[] passChars = (new String(password)).toCharArray();
		PBEKeySpec spec = new PBEKeySpec(passChars, salt, _iterations, 512);
		SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		return secKeyFact.generateSecret(spec).getEncoded();
	}
	
	private byte[] hashSecret(byte[] secret) throws Exception{
		Logger.debug(this, "hashSecret");
		return KeyManager.getInstance().generateKey(secret).getEncoded(); // TODO: move salt to KeyManager?
	}
	
	public byte[] generatePasswordHash(byte[] password, byte[] salt) throws Exception {
		Logger.debug(this, "generatePasswordHash");
		return hashSecret(generateSecret(password, salt));
	}
	
	// Assign original hash which is created at password creation
	public void setPasswordHash(byte[] passwordHash) {
		Logger.debug(this, "setPasswordHash");
		_passwordHash = passwordHash;
	}
	
	/** Accepts password to hash then compares with hash on file*/
	public Boolean verifyPassword(byte[] password) throws Exception {
		Logger.debug(this, "verifyPassword");
		byte [] newHash = generatePasswordHash(password, KeyManager.getInstance().getSalt());
		Logger.debug(this, "New Hash 		== " + new String(newHash));
		Logger.debug(this, "Current Hash 	== " + new String(getPasswordHash()));
		return isPasswordHashEqual(newHash);
	}
	
	private Boolean isPasswordHashEqual(byte[] passwordHash) throws Exception {
		Logger.debug(this, "isPasswordHashEqual");
		return new String(_passwordHash).equals(new String(passwordHash));
	}
	
	public Boolean passwordHashFileExists() {
		Logger.debug(this, "passwordHashFileExists");	
		return FileManager.getInstance().fileExists(_hashFileName);
	}

	public byte[] getPasswordHash() {
		Logger.debug(this, "getPasswordHash");
		return _passwordHash;
	}
	
	public String getPasswordHashAsHex() throws NoSuchAlgorithmException {
		Logger.debug(this, "getPasswordHashAsHex");
		return toHex(_passwordHash);
	}
	
	/** Converts byte array to hex String : easy to view*/
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
