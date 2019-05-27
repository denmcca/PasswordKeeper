package testing;


import java.util.Date;


import application.Decrypter;
import application.EncryptedPacket;
import application.Encrypter;
import application.FileManager;
import application.KeyManager;
import application.Logger;
import application.LoginManager;
import application.PasswordManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptionTest {
	KeyManager km;
	PasswordManager pm;
	LoginManager lm;
	
	@Test
	void test_encryption() throws Exception {
		Logger.debug(this, "Begin test_encryption");
		km = KeyManager.getInstance();
		pm = PasswordManager.getInstance();
		lm = LoginManager.getInstance();
		km.load();
		lm.sendPasswordToKeyManager("password".getBytes());
		String test = new String("This is a string");
		
		EncryptedPacket output = Encrypter.getInstance().encrypt(test.getBytes());
		Decrypter decrypter = Decrypter.getInstance();
		byte[] decrypted_output = decrypter.decrypt(output);
		System.out.println("Input == " + test);
		System.out.println("Encrypted input == " + new String(output.getData()));
		System.out.println("Decrypted input == " + new String(decrypted_output));
		assertEquals(test, new String(decrypted_output));
	}
	
	@Test
	void test_file_manager() throws Exception {
		Logger.debug(this, "Begin test_file_manager");
		FileManager fileManager = FileManager.getInstance();
		String filename = new String(new Date().getTime() + ".storage");
		fileManager.init(filename);
		byte[] data = "This is a test".getBytes();
		fileManager.writeData(data, filename, true);
		String output = new String(fileManager.readData(filename));
		assertEquals(new String(data), new String(output));
	}
}
