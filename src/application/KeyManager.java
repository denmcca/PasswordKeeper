package application;

import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyManager {
	private final int iterationCount = 65536;
	private final int keyLength = 256;
	private final String factoryAlgo = "PBKDF2WithHmacSHA1";
	private final String secretKeySpecAlgo = "AES";
	
	private static KeyManager _keyManager = null;
	private KeyStore keyStore = null; // TODO: implement KeyStore to store keys
	private SecretKey _sessionKey;
	private byte[] _salt = null;
	private String _saltFileName = "salt.storage";
	
	/** Constructor for Singleton KeyManager */
	private KeyManager() throws IOException, NoSuchAlgorithmException {
		Logger.debug(this, "constructor");
		load();
	}
	
	/** Returns only instance of KeyManager */
	public static KeyManager getInstance() throws IOException,
			NoSuchAlgorithmException {
		if (_keyManager == null) {
			_keyManager = new KeyManager();
		}
		return _keyManager;
	}
	
	/** Call function to load salt from file if current salt is null.*/
	public void load() throws IOException, NoSuchAlgorithmException {
		Logger.debug(this, "load");
		if (_salt == null) {
			loadSalt();
		}
	}
	
	/** Calls function to save current salt to file. If current salt 
	 * is null, salt will be generated.*/
	public void save() throws Exception{
		Logger.debug(this, "save");
		if (_salt == null) {
			_salt = generateSalt();
		}
		saveSalt();
	}
	
	/** Gets salt from file. Throws IOException if not found */
	private void loadSalt() throws IOException, NoSuchAlgorithmException {
		Logger.debug(this, "loadSalt");
		if (!FileManager.getInstance().fileExists(_saltFileName)){
			_salt = generateSalt();
			saveSalt();
		} else {
			_salt = FileManager.getInstance().readData(_saltFileName);
		}
	}

	private void createSalt() throws IOException, NoSuchAlgorithmException {
		Logger.debug(this, "Salt file not found. Generating new salt.");
		_salt = generateSalt();
		saveSalt();
	}

	/** Saves current salt to file if current salt is not null */
	private void saveSalt() throws IOException {
		Logger.debug(this, "saveSalt");
		FileManager.getInstance().writeData(_salt, _saltFileName, false);
	}
	
	/** Takes in password and calls function to generate new key
	 * then sets key as current session key.*/
	private void assignSessionKey(byte[] password) throws InvalidKeySpecException, NoSuchAlgorithmException {
		Logger.debug(this, "assignSessionKey");
		setSessionKey(generateKey(password));
	}
	
	/** Accepts SecretKey and assigns as session key.*/
	private void setSessionKey(SecretKey sKey) {
		Logger.debug(this, "setSessionKey");
		_sessionKey = sKey;
	}
	
	/** Accepts password and generates secret key 
	 * using PBKDF2WithHmacSHA1 and AES.*/
	public SecretKey generateKey(byte[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		Logger.debug(this, "generateKey");
		SecretKeyFactory skf = SecretKeyFactory.getInstance(factoryAlgo);
		KeySpec spec = new PBEKeySpec(
				(new String(password)).toCharArray(), 
				_salt,
				iterationCount, keyLength);
		SecretKey sKey = skf.generateSecret(spec);
		return new SecretKeySpec(sKey.getEncoded(), secretKeySpecAlgo);
	}
	
	/** Returns current session key. Throws NullPointerException if null */
	public SecretKey getSessionKey() {
		Logger.debug(this, "getSessionKey");
		if (_sessionKey == null) throw new NullPointerException();
		return _sessionKey;
	}
	
	/** Accepts password and calls method that to generate secret key
	 * and assign it as current session key.*/
	public void receiveUserPassword(byte[] password) throws InvalidKeySpecException, NoSuchAlgorithmException {
		Logger.debug(this, "receiveUserPassword");
		assignSessionKey(password);
	}
	
	/** Gets salt using random generator.*/
	public byte[] generateSalt() throws NoSuchAlgorithmException {
		Logger.debug(this, "generateSalt");
		SecureRandom secRan = SecureRandom.getInstance("SHA1PRNG");
		byte [] salt = new byte[16];
		secRan.nextBytes(salt);
		return salt;
	}
	
	/** Accepts salt and sets it as current salt.*/
	public void setSalt(byte[] salt) {
		Logger.debug(this, "setSalt");
		_salt = salt;
	}
	
	/** Returns current salt.*/
	public byte[] getSalt() throws IOException, NoSuchAlgorithmException {
		Logger.debug(this, "getSalt");
		if (!FileManager.getInstance().fileExists(_saltFileName)){
			_salt = generateSalt();
			saveSalt();
		}
		if (_salt == null){
			loadSalt();
		}
		return _salt;
	}
	
}

