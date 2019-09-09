package managers;

import utils.Logger;
import components.Decrypter;
import components.EncryptedPacket;
import components.Encrypter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EncryptionManager {
	private static EncryptionManager instance = null;
	private Encrypter encrypter;
	private Decrypter decrypter;
	
	private EncryptionManager() {
		Logger.debug(this, "constructor");
		encrypter = Encrypter.getInstance();
		decrypter = Decrypter.getInstance();
	}

	/**
	 * Singleton
	 * @return instance of Encryption Manager
	 */
	public static EncryptionManager getInstance() {
		if (instance == null) {
			instance = new EncryptionManager();
		} 
		return instance;
	}
	
	public EncryptedPacket encrypt(byte[] bytes) {
		Logger.debug(this, "encrypt");
		return encrypter.encrypt(bytes);
	}
	
	public byte[] decrypt(EncryptedPacket encryptedPacket) {
		Logger.debug(this, "decrypt");
		return decrypter.decrypt(encryptedPacket);
	}
	
	public byte[] convertToBytes(EncryptedPacket ePacket) {
		Logger.debug(this, "convertToBytes");
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOS = new ObjectOutputStream(byteArrayOS);
			objectOS.writeObject(ePacket);
			objectOS.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return byteArrayOS.toByteArray();
	}
	
	public EncryptedPacket convertToEncryptedPacket(byte[] bytes) {
		Logger.debug(this, "convertToEncryptedPacket");
		ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(bytes);
		EncryptedPacket encryptedPacket = null;
		try {
			ObjectInputStream objectIS = new ObjectInputStream(byteArrayIS);
			encryptedPacket = (EncryptedPacket)objectIS.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return encryptedPacket;
	}
}
