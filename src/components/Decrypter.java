package components;

import utils.Logger;
import managers.KeyManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Decrypter {
	
	private static Decrypter _decrypter = null;
	private Cipher _cipher = null;
	
	private Decrypter() {}
	
	private void init(byte[] iv) {
		Logger.debug(this, "init");
		try {
			_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			_cipher.init(Cipher.DECRYPT_MODE, KeyManager.getInstance().getSessionKey(), new IvParameterSpec(iv));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException |
				InvalidAlgorithmParameterException | InvalidKeyException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Logger.debug(this,"Session Key Format: " + KeyManager.getInstance().getSessionKey().getFormat());
	}
	
	public static Decrypter getInstance() {
		if (_decrypter == null) {
			_decrypter = new Decrypter();
		}
		return _decrypter;
	}
	
	public byte[] decrypt(EncryptedPacket input) {
		Logger.debug(this, "decrypt");
		init(input.getIV());
		byte[] bytes = null;
		try {
			bytes = _cipher.doFinal(input.getData());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return bytes;
	}
}
