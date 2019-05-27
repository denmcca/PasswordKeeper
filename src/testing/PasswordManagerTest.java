package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.Test;
import application.KeyManager;
import application.Logger;
import application.PasswordManager;

class PasswordManagerTest {
	private KeyManager km;
	private PasswordManager pm;
	
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
		
		Logger.debug(this, "getPass == " + new String(pass));
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
		Logger.debug(this, "getPass     == " + new String(pass));
		Logger.debug(this, "testHash == " + new String(testHash));
		assertEquals(new String(pass), new String(testHash));
	}
	
	@Test
	void test2_password_hash_file_exists() throws Exception {
		Logger.debug(this, "Begin test_password_hash_file_exists");

		PasswordManager pm = PasswordManager.getInstance();
		assertEquals(true, PasswordManager.getInstance().passwordHashFileExists());
	}

	@Test
	void test4_create_hash_restore_hash() throws IOException,
			NoSuchAlgorithmException, InvalidKeySpecException {
		Logger.debug(this, "Begin test4_create_hash_restore_hash");
		KeyManager.getInstance().load();
		byte[] hashToStore = PasswordManager.getInstance().generatePasswordHash("password".getBytes(),
				KeyManager.getInstance().getSalt());
		PasswordManager.getInstance().setPasswordHash(hashToStore);
		PasswordManager.getInstance().save();
		byte[] hashToCompare = PasswordManager.getInstance().generatePasswordHash("password".getBytes(),
				KeyManager.getInstance().getSalt());
		PasswordManager.getInstance().load();
		assertEquals(new String(PasswordManager.getInstance().getPasswordHash()),
				new String(hashToCompare));
	}

}
