package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import application.DataManager;
import application.InputManager;
import application.KeyManager;
import application.Logger;
import application.LoginManager;
import application.PwData;


class DataManagerTest {
	DataManager dm;
	LoginManager lm;
	KeyManager km;
	
	@Test
	void test1_add_sort_getall() throws Exception {
		Logger.debug(this,"Begin test1_add_sort_getall");
		dm = DataManager.getInstance();
		populateDataManager();
		for (PwData data : dm.getAllData()) {
			System.out.println(data.toString());
		}
		assertEquals(true, true);
	}
	
	
	@Test
	void test2_save_loads() throws Exception {
		Logger.debug(this,"Begin test2_save_load");
		lm = LoginManager.getInstance();
		km = KeyManager.getInstance();
		populateDataManager();
		km.load();
		lm.sendPasswordToKeyManager("password".getBytes());
		dm.saveData();
		dm.loadData();
		for (PwData data : dm.getAllData()) {
			System.out.println(data.toString());
		}
		System.out.println();
//		test3_print_data_from_encrypted_file();
	}
	
	@Test
	void test3_save_file() throws Exception {
		Logger.debug(this,"Begin test3_save_file");
		km = KeyManager.getInstance();
		km.load();
		lm = LoginManager.getInstance();
		lm.sendPasswordToKeyManager("password".getBytes());
		dm = DataManager.getInstance();
		populateDataManager();
		dm.saveData();
	}
	
	@Test
	void test4_load_file() throws Exception {
		Logger.debug(this,"Begin test4_load_file");
		km = KeyManager.getInstance();
		km.load();
		lm = LoginManager.getInstance();
		lm.sendPasswordToKeyManager("password".getBytes());
		dm = DataManager.getInstance();
		dm.loadData();
		for (PwData data : dm.getAllData()) {
			System.out.println(data.toString());
		}
		System.out.println();
	}
	
	@Test
	void test5_print_data_from_encrypted_file() throws Exception {
		Logger.debug(this,"Begin test3_print_data_from_encrypted_file");
		km = KeyManager.getInstance();
		km.load();
		lm = LoginManager.getInstance();
		lm.sendPasswordToKeyManager("password".getBytes());
		dm = DataManager.getInstance();
		dm.loadData();
		for (PwData data : dm.getAllData()) {
			data.toString();
		}
	}
	
	@Test
	void test6_vector_contains() throws Exception {
		Logger.debug(this,"Begin test6_vector_contains");		
		km = KeyManager.getInstance();
		lm = LoginManager.getInstance();
		dm = DataManager.getInstance();
		initiateKeyLoginManager();
		populateDataManager();	
		PwData data = this.createCredential("a","b","c");
		this.addCredentialToDM(data);
		this.addCredentialToDM(data);// equals compares by object address
		this.printDataManagerData();
	}
	
	@Test
	void test7_load_add_print_save() throws Exception {
		Logger.debug(this,"test7_load_add_print_save");
		km = KeyManager.getInstance();
		lm = LoginManager.getInstance();
		dm = DataManager.getInstance();
		this.initiateKeyLoginManager();
		km.load();
		lm.sendPasswordToKeyManager("password".getBytes());
		dm.loadData();
		dm.add(this.createCredential("a", "b", "c"));
		dm.add(this.createCredential("a", "b", "c"));
		this.printDataManagerData();
		dm.saveData();		
	}
	
//	@Test
//	void test8_remove_
	
	private void populateDataManager() throws InterruptedException {
		Logger.debug(this,"populateDataManager");
		PwData data1 = new PwData();
		data1.platform("platformA");
		data1.login("LoginA");
		data1.pass("SecretA");
		PwData data5 = new PwData();
		data5.platform("platformB");
		data5.login("LoginA");
		data5.pass("SecretZ");
		Thread.sleep(10);
		PwData data2 = new PwData();
		data2.platform("platformB");
		data2.login("LoginB");
		data2.pass("SecretA");
		PwData data3 = new PwData();
		data3.platform("platformC");
		data3.login("LoginC");
		data3.pass("SecretC");
		PwData data4 = new PwData();
		data4.platform("platformB");
		data4.login("LoginA");
		data4.pass("SecretA");
		dm.add(data1);
		dm.add(data2);
		dm.add(data3);
		dm.add(data4);
		dm.add(data5);
		dm.sort();
	}
	
	private void initiateKeyLoginManager() throws Exception {
		Logger.debug(this,"initiateKeyLoginManager");
		km.load();
		lm.sendPasswordToKeyManager("password".getBytes());
	}
	
	private void printDataManagerData() {
		Logger.debug(this,"printDataManagerData");
		Logger.debug(this,"Number of elements in data vector: " + dm.getAllData().size());
		for (PwData data : dm.getAllData()) {
			System.out.println(data.toString());
		}
		System.out.println();
	}
	
	private PwData createCredential(String plat, String login, String pass) {
		Logger.debug(this,"createCredential");
		PwData data = new PwData();
		data.platform(plat);
		data.login(login);
		data.pass(pass);
		return data;
	}
	
	private void addCredentialToDM(Object data) {
		Logger.debug(this,"addCredentialToDM");
		if (dm.add((PwData)data)) {
			Logger.debug(this,"Credential added");
			return;
		}
		Logger.debug(this,"Credential not added");
	}
}
