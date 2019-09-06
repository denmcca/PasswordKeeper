package managers;

import utils.Logger;
import components.Decrypter;
import components.EncryptedPacket;
import components.Encrypter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncryptionManager {
	private static EncryptionManager _encryptionManager = null;
	
	private EncryptionManager() {
		Logger.debug(this, "constructor");
	}
	
	public static EncryptionManager getInstance() {
		if (_encryptionManager == null) {
			_encryptionManager = new EncryptionManager();
		} 
		return _encryptionManager;
	}
	
	public EncryptedPacket encrypt(byte[] bytes) throws Exception {
		Logger.debug(this, "encrypt");
		return Encrypter.getInstance().encrypt(bytes);
	}
	
	public byte[] decrypt(EncryptedPacket encryptedPacket) throws BadPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, InvalidKeyException, IOException {
		Logger.debug(this, "decrypt");
		return Decrypter.getInstance().decrypt(encryptedPacket);
	}
	
	public byte[] convertToBytes(EncryptedPacket ePacket) throws IOException {
		Logger.debug(this, "convertToBytes");
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		ObjectOutputStream objectOS = new ObjectOutputStream(byteArrayOS);
		objectOS.writeObject(ePacket);
		objectOS.flush();
		return byteArrayOS.toByteArray();
	}
	
	public EncryptedPacket convertToEncryptedPacket(byte[] bytes)
			throws IOException, ClassNotFoundException {
		Logger.debug(this, "convertToEncryptedPacket");
		ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(bytes);
		ObjectInputStream objectIS = new ObjectInputStream(byteArrayIS);
		return (EncryptedPacket)objectIS.readObject();
	}
}
