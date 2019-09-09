package testing;


import java.util.Date;


import components.Decrypter;
import components.EncryptedPacket;
import components.Encrypter;
import managers.FileManager;
import managers.KeyManager;
import utils.Logger;
import managers.LoginManager;
import managers.PasswordManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptionTest {
	private KeyManager km;
	private PasswordManager pm;
	private LoginManager lm;
	
	@Test
	void test_encryption() {
		Logger.debug(this, "Begin test_encryption");
		km = KeyManager.getInstance();
		pm = PasswordManager.getInstance();
		lm = LoginManager.getInstance();
		km.load();
		lm.sendPasswordManagerPassword("password".getBytes());
		String test = "This is a string";
		
		EncryptedPacket output = Encrypter.getInstance().encrypt(test.getBytes());
		Decrypter decrypter = Decrypter.getInstance();
		byte[] decrypted_output = decrypter.decrypt(output);
		System.out.println("Input == " + test);
		System.out.println("Encrypted input == " + new String(output.getData()));
		System.out.println("Decrypted input == " + new String(decrypted_output));
		assertEquals(test, new String(decrypted_output));
	}
	
	@Test
	void test_file_manager() {
		Logger.debug(this, "Begin test_file_manager");
		FileManager fileManager = FileManager.getInstance();
		String filename = new Date().getTime() + ".storage";
		fileManager.init(filename);
		byte[] data = "This is a test".getBytes();
		fileManager.writeData(data, filename, true);
		String output = new String(fileManager.readData(filename));
		assertEquals(new String(data), output);
	}
}
