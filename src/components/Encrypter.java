package components;

import utils.Logger;
import managers.KeyManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Encrypter {

	private static Encrypter _encrypter = null;
	private Cipher _cipher;
	
	private Encrypter() {
		init();
	}
	
	private void init() {
		Logger.debug(this, "init");
		try {
			_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			_cipher.init(Cipher.ENCRYPT_MODE, KeyManager.getInstance().getSessionKey());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static Encrypter getInstance() {
		if (_encrypter == null) {
			_encrypter = new Encrypter();
		}
		return _encrypter;
	}
	
	public EncryptedPacket encrypt(byte[] input) {
		Logger.debug(this, "encrypt");
		EncryptedPacket ePacket = null;
		try {
			ePacket = new EncryptedPacket(_cipher.getIV(), _cipher.doFinal(input));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return ePacket;
	}
}