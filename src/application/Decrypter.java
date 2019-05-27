package application;

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
	
	private void init(byte[] iv) throws NoSuchPaddingException,
			NoSuchAlgorithmException, IOException,
			InvalidAlgorithmParameterException, InvalidKeyException {
		Logger.debug(this, "init");
		_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		Logger.debug(this,"Session Key Format: " + KeyManager.getInstance().getSessionKey().getFormat());
		_cipher.init(Cipher.DECRYPT_MODE, KeyManager.getInstance().getSessionKey(), new IvParameterSpec(iv));
	}
	
	public static Decrypter getInstance() {
		if (_decrypter == null) {
			_decrypter = new Decrypter();
		}
		return _decrypter;
	}
	
	public byte[] decrypt(EncryptedPacket input)
			throws BadPaddingException, IllegalBlockSizeException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, IOException {
		Logger.debug(this, "decrypt");
		init(input.getIV());
		return _cipher.doFinal(input.getData());
	}
}
