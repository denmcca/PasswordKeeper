package managers;

import utils.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
	
	public static PasswordManager getInstance() {
		if (_passwordManager == null) {
			_passwordManager = new PasswordManager();
		}
		return _passwordManager;
	}

	// Public interface to load password hash file. If file does not exist
	// throws IOException
	public void load() throws IOException {
		Logger.debug(this, "load");
		loadPasswordHash();
	}

	// Public interface to save current password hash in memory to file. If
	// password hash is not in memory IOException is thrown.
	public void save() throws IOException {
		Logger.debug(this, "save");
		savePasswordHash();
	}

	// Load hash from file
	private void loadPasswordHash() throws IOException {
		Logger.debug(this, "loadPasswordHashFile");
		if (_passwordHash == null) {
			_passwordHash = FileManager.getInstance().readData(_hashFileName);
		}
	}

	// Save hash to file
	private void savePasswordHash() throws IOException {
		Logger.debug(this, "savePasswordHashFile");
		if (_passwordHash == null) Logger.debug(this, "_passwordHash is null!");
		FileManager.getInstance().writeData(_passwordHash, _hashFileName, false);
	}

	/** Accepts password to hash then compares with hash on file*/
	public Boolean verifyPassword(byte[] password) throws IOException,
			InvalidKeySpecException, NoSuchAlgorithmException {
		Logger.debug(this, "verifyPassword");
		PasswordManager.getInstance().load();
		KeyManager.getInstance().load();
		byte [] newHash = generatePasswordHash(password, KeyManager.getInstance().getSalt());
		Logger.debug(this, "New Hash 		== " + toHex(newHash));
		Logger.debug(this, "Current Hash 	== " + toHex(getPasswordHash()));
		return isPasswordHashEqual(newHash);
	}

	public void sendPasswordToKeyManager(byte[] password) throws Exception {
		Logger.debug(this, "sendPasswordToKeyManager");
		KeyManager.getInstance().receiveUserPassword(password);
	}
	// Generates hash. Hash generation also used to create hash at getLogin.

	private byte[] generateSecret(byte[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException { // salt as parameter to promote decoupling
		Logger.debug(this, "generateSecret");
		char[] passChars = (new String(password)).toCharArray();
		PBEKeySpec spec = new PBEKeySpec(passChars, salt, _iterations, 512);
		SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		return secKeyFact.generateSecret(spec).getEncoded();
	}

	private byte[] hashSecret(byte[] password) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		Logger.debug(this, "hashSecret");
		return KeyManager.getInstance().generateKey(password).getEncoded(); // TODO: move salt to KeyManager?
	}

	/** Generates and returns password hash */
	public byte[] generatePasswordHash(byte[] password, byte[] salt)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		Logger.debug(this, "generatePasswordHash");
		Logger.debug(this, "password as bytes: " + new String(password));
		return hashSecret(generateSecret(password, salt));
	}
	// Assign original hash which is created at password creation

	public void setPasswordHash(byte[] passwordHash) {
		Logger.debug(this, "setPasswordHash");
		_passwordHash = passwordHash;
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
}
