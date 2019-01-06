package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EncryptionManager {
	private static EncryptionManager _encryptionManager = null;
	
	private EncryptionManager() {
		Logger.debug(this, "constructor");
	}
	
	public static EncryptionManager getInstance() throws Exception {
		if (_encryptionManager == null) {
			_encryptionManager = new EncryptionManager();
		} 
		return _encryptionManager;
	}
	
	public EncryptedPacket encrypt(byte[] bytes) throws Exception {
		Logger.debug(this, "encrypt");
		return Encrypter.getInstance().encrypt(bytes);
	}
	
	public byte[] decrypt(EncryptedPacket encryptedPacket) throws Exception {
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
	
	public EncryptedPacket convertToEncryptedPacket(byte[] bytes) throws IOException, ClassNotFoundException {
		Logger.debug(this, "convertToEncryptedPacket");
		ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(bytes);
		ObjectInputStream objectIS = new ObjectInputStream(byteArrayIS);
		return (EncryptedPacket)objectIS.readObject();
	}
}
