package components;

import utils.Logger;
import managers.KeyManager;

import javax.crypto.Cipher;

public class Encrypter {

	private static Encrypter _encrypter = null;
	private Cipher _cipher;
	
	private Encrypter() throws Exception {
		init();
	}
	
	private void init() throws Exception {
		Logger.debug(this, "init");
		_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		_cipher.init(Cipher.ENCRYPT_MODE, KeyManager.getInstance().getSessionKey());
	}
	
	public static Encrypter getInstance() throws Exception {
		if (_encrypter == null) {
			_encrypter = new Encrypter();
		}
		return _encrypter;
	}
	
	public EncryptedPacket encrypt(byte[] input) throws Exception {
		Logger.debug(this, "encrypt");
		EncryptedPacket ePacket = new EncryptedPacket(_cipher.getIV(), _cipher.doFinal(input));
		return ePacket;
	}
}