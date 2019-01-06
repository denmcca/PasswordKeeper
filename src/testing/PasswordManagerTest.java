package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import application.KeyManager;
import application.Logger;
import application.PasswordManager;

class PasswordManagerTest {
	KeyManager km;
	PasswordManager pm;
	
	@Test
	void test1_new_hashes() throws Exception {
		Logger.debug(this, "Begin test_new_hashes");

		pm = PasswordManager.getInstance();
		km = KeyManager.getInstance();
		km.setSalt(km.generateSalt());
		km.save();
		byte[] hash = pm.generatePasswordHash("password".getBytes(), km.getSalt());
		pm.setPasswordHash(hash);
		pm.save(); // saves password hash to file
		pm.setPasswordHash(null);
		pm.load(); // loads password hash from file
		byte[] pass = pm.getPasswordHash();
		
		Logger.debug(this, "pass == " + new String(pass));
		Logger.debug(this, "hash == "+ new String(hash));
		
		Logger.debug(this, "Hash to Hex: " + pm.getPasswordHashAsHex());
		
		assertEquals(true, pm.verifyPassword("password".getBytes()));
	}
	
	@Test
	void test3_load_existing_files() throws Exception {
		Logger.debug(this, "Begin test_load_existing_files");
		pm = PasswordManager.getInstance();
		km = KeyManager.getInstance();
		Thread.sleep(10); // needed to allow other tests to close file
		pm.load();
		km.load();
		byte[] pass = pm.getPasswordHash();
		byte[] testHash = pm.generatePasswordHash("password".getBytes(), km.getSalt());
		Logger.debug(this, "pass     == " + new String(pass));
		Logger.debug(this, "testHash == " + new String(testHash));
		assertEquals(new String(pass), new String(testHash));
	}
	
	@Test
	void test2_password_hash_file_exists() throws Exception {
		Logger.debug(this, "Begin test_password_hash_file_exists");

		PasswordManager pm = PasswordManager.getInstance();
		assertEquals(true, PasswordManager.getInstance().passwordHashFileExists());
	}

}
