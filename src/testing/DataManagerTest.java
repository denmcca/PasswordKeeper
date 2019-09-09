package testing;

import static org.junit.jupiter.api.Assertions.*;

import components.PwData;
import managers.DataManager;
import managers.KeyManager;
import managers.LoginManager;
import org.junit.jupiter.api.Test;
import utils.Logger;


class DataManagerTest {
	private DataManager dm;
	private LoginManager lm;
	private KeyManager km;
	
	@Test
	void test1_add_sort_getall() throws Exception {
		Logger.debug(this,"Begin test1_add_sort_getall");
		dm = DataManager.getInstance();
		populateDataManager();
		for (PwData data : dm.getAllData()) {
			System.out.println(data.toString());
		}
		assertTrue(true);
	}
	
	
	@Test
	void test2_save_loads() throws Exception {
		Logger.debug(this,"Begin test2_save_load");
		lm = LoginManager.getInstance();
		km = KeyManager.getInstance();
		populateDataManager();
		km.load();
		lm.sendPasswordManagerPassword("password".getBytes());
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
		km.getSalt();
		lm = LoginManager.getInstance();
		lm.sendPasswordManagerPassword("password".getBytes());
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
		lm.sendPasswordManagerPassword("password".getBytes());
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
		lm.sendPasswordManagerPassword("password".getBytes());
		dm = DataManager.getInstance();
		dm.loadData();
		for (PwData data : dm.getAllData()) {
			System.out.println(data.toString());
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
		lm.sendPasswordManagerPassword("password".getBytes());
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
		data1.setPlatform("linkedin.com");
		data1.setLogin("useryou@umail.com");
		data1.setPass("linkedinpassword");
		PwData data5 = new PwData();
		data5.setPlatform("gmail.com");
		data5.setLogin("basicuser@gmail.com");
		data5.setPass("reallylongpassword");
		Thread.sleep(10);
		PwData data2 = new PwData();
		data2.setPlatform("csulb.edu");
		data2.setLogin("student.name@csulb.edu");
		data2.setPass("Fdi21edEdcS");
		PwData data3 = new PwData();
		data3.setPlatform("sandwiches.com");
		data3.setLogin("iluvsandwiches");
		data3.setPass("especiallywithmayo");
		PwData data4 = new PwData();
		data4.setPlatform("nocreativity.com");
		data4.setLogin("randomlogin");
		data4.setPass("withnotsorandompassword");
		dm = DataManager.getInstance();
		dm.add(data1);
		dm.add(data2);
		dm.add(data3);
		dm.add(data4);
		dm.add(data5);
		dm.sort();
		for (Object datum : dm.getAllData()){
			Logger.log(datum.toString());
		}
	}
	
	private void initiateKeyLoginManager() {
		Logger.debug(this,"initiateKeyLoginManager");
		km.load();
		lm.sendPasswordManagerPassword("password".getBytes());
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
		data.setPlatform(plat);
		data.setLogin(login);
		data.setPass(pass);
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
