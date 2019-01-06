package application;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class Decrypter {
	
	private static Decrypter _decrypter = null;
	private Cipher _cipher = null;
	
	private Decrypter() throws Exception {}
	
	private void init(byte[] iv) throws Exception {
		Logger.debug(this, "init");
		_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		Logger.debug(this,"Session Key Format: " + KeyManager.getInstance().getSessionKey().getFormat());
		_cipher.init(Cipher.DECRYPT_MODE, KeyManager.getInstance().getSessionKey(), new IvParameterSpec(iv));
	}
	
	public static Decrypter getInstance() throws Exception {
		if (_decrypter == null) {
			_decrypter = new Decrypter();
		}
		return _decrypter;
	}
	
	public byte[] decrypt(EncryptedPacket input) throws Exception {
		Logger.debug(this, "decrypt");
		init(input.getIV());
		return _cipher.doFinal(input.getData());
	}
}
