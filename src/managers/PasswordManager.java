package managers;

import utils.Logger;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordManager {
	
	private static PasswordManager instance = null;
	private KeyManager keyManager;
	private byte[] _passwordHash = null;
	private int _iterations = 65536;
	private String _hashFileName = "hash.storage";

	
	private PasswordManager() {
		Logger.debug(this, "constructor");
		keyManager = KeyManager.getInstance();
	}
	
	public static PasswordManager getInstance() {
		if (instance == null) {
			instance = new PasswordManager();
		}
		return instance;
	}

	// Public interface to load password hash file. If file does not exist
	// throws IOException
	public void load() {
		Logger.debug(this, "load");
		loadPasswordHash();
	}

	// Public interface to save current password hash in memory to file. If
	// password hash is not in memory IOException is thrown.
	public void save() {
		Logger.debug(this, "save");
		savePasswordHash();
	}

	// Load hash from file
	private void loadPasswordHash() {
		Logger.debug(this, "loadPasswordHashFile");
		if (_passwordHash == null) {
			_passwordHash = FileManager.getInstance().readData(_hashFileName);
		}
	}

	// Save hash to file
	private void savePasswordHash() {
		Logger.debug(this, "savePasswordHashFile");
		if (_passwordHash == null) Logger.debug(this, "_passwordHash is null!");
		FileManager.getInstance().writeData(_passwordHash, _hashFileName, false);
	}

	/*
		Accepts password to hash then compares with hash on file
		Returns true if password is correct.
	*/
	public Boolean verifyPassword(byte[] password) {
		Logger.debug(this, "verifyPassword");
		PasswordManager.getInstance().load();
		KeyManager.getInstance().load();
		byte [] newHash = generatePasswordHash(password, KeyManager.getInstance().getSalt());
		Logger.debug(this, "New Hash 		== " + toHex(newHash));
		Logger.debug(this, "Current Hash 	== " + toHex(getPasswordHash()));
		return isPasswordHashEqual(newHash);
	}

	public void sendPasswordToKeyManager(byte[] password) {
		Logger.debug(this, "sendPasswordToKeyManager");
		KeyManager.getInstance().receiveUserPassword(password);
	}
	// Generates hash. Hash generation also used to create hash at getLogin.

	private byte[] generateSecret(byte[] password, byte[] salt) { // salt as parameter to promote decoupling
		Logger.debug(this, "generateSecret");
		char[] passChars = (new String(password)).toCharArray();
		PBEKeySpec spec = new PBEKeySpec(passChars, salt, _iterations, 512);
		byte[] secKey = null;
		try {
			SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			secKey = secKeyFact.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return secKey;
	}

	private byte[] hashSecret(byte[] password) {
		Logger.debug(this, "hashSecret");
		return KeyManager.getInstance().generateKey(password).getEncoded(); // TODO: move salt to KeyManager?
	}

	/** Generates and returns password hash */
	public byte[] generatePasswordHash(byte[] password, byte[] salt) {
		Logger.debug(this, "generatePasswordHash");
		Logger.debug(this, "password as bytes: " + new String(password));
		return hashSecret(generateSecret(password, salt));
	}
	// Assign original hash which is created at password creation

	public void setPasswordHash(byte[] password) {
		Logger.debug(this, "setPasswordHash");
		_passwordHash = generatePasswordHash(password, keyManager.getSalt());
	}

	private Boolean isPasswordHashEqual(byte[] passwordHash) {
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

	public String getPasswordHashAsHex() {
		Logger.debug(this, "getPasswordHashAsHex");
		return toHex(_passwordHash);
	}

	/** Converts byte array to hex String : easy to view*/
    private static String toHex(byte[] array)
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

	public void receiveUserPassword(byte[] password) {
    	keyManager.receiveUserPassword(password);
	}
}
