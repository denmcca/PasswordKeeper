package managers;

import utils.Logger;
import components.PwData;
import components.EncryptedPacket;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Vector;

@SuppressWarnings("serial")
public class DataManager implements Serializable {
	private static DataManager _dataManager = null;
	private Vector<PwData> _data;
	private String _dataFileName = "data.storage";
	
	private DataManager() {
		Logger.debug(this, "Constructor");
		_data = new Vector<>();
	}
	
	public static DataManager getInstance() {
		if (_dataManager == null) {
			_dataManager = new DataManager();
		}
		return _dataManager;
	}
	
	public boolean add(PwData data) {
		Logger.debug(this, "add");
		if (_data.contains(data)) {
			Logger.log("The credentials entered already exist.");
			Logger.log("No credentials were added.");
			return false;
		}
		_data.addElement(data);
		sort();
//		for (PwData datum : _data){
//			Logger.log("\n" + datum.toString());
//		}
		return true;
	}
	
	public boolean remove(PwData data) {
		Logger.debug(this, "remove");
		return _data.remove(data);
	}
	
	public Vector<PwData> findByPlatform(String platform) {
		Logger.debug(this, "findByPlatform");
		Vector<PwData>container = new Vector<PwData>();
		for(PwData data : _data) {
			if (data.getPlatform().equals(platform))
				container.add(data);
		}
		return container;
	}
	
	public Boolean credentialExists(PwData data) {
		Logger.debug(this, "credentialExists");
		return _data.contains(data);
	}
	
	public void sort() {
		Logger.debug(this, "sort");
		Collections.sort(_data);
	}
	
	public Vector<PwData> getAllData() {
		Logger.debug(this, "getAllData");
		Logger.debug(this, "\n" + _data.toString());
		return _data;
	}
	
	/** Converts current DataManager and its state to
	 * a ready-to-encrypt byte array */
	private byte[] convertToBytes(DataManager dataManager) throws IOException {
		Logger.debug(this, "convertToBytes");
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		ObjectOutputStream objectOS = new ObjectOutputStream(byteArrayOS);
		objectOS.writeObject(dataManager);
		objectOS.flush();
		return byteArrayOS.toByteArray();
	}
	
	/** Accepts decrypted byte array of DataManager object 
	 * then converts to restore to usable object */
	private DataManager convertToDataManager(byte[] bytes) throws IOException, ClassNotFoundException {
		Logger.debug(this, "convertToDataManager");
		ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(bytes);
		ObjectInputStream objectIS = new ObjectInputStream(byteArrayIS);
		return (DataManager)objectIS.readObject();
	}
	
	/** Saves data from data container to disk drive */
	public void saveData() throws Exception { // TODO: decouple? Only _dataFileName belongs to DataManager
		Logger.debug(this, "saveData");
		EncryptionManager em = EncryptionManager.getInstance();
		byte[] data = convertToBytes(_dataManager);
		EncryptedPacket encData = em.encrypt(data);
		FileManager.getInstance().writeData(em.convertToBytes(encData), _dataFileName, false);
	}
	
	/** Retrieves file from disk drive */
	public void loadData() throws IOException, ClassNotFoundException, BadPaddingException,
			InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException,
			NoSuchPaddingException, InvalidAlgorithmParameterException {
		Logger.debug(this, "loadData");
		EncryptionManager em = EncryptionManager.getInstance(); // TODO: decouple
		byte[] data = FileManager.getInstance().readData(_dataFileName);
		if (data.length < 1) {
			Logger.log("No data to load.");
			return;
		}
		Logger.debug(this, "length == " + data.length);
		data = em.decrypt(em.convertToEncryptedPacket(data));
		_dataManager._data = convertToDataManager(data)._data;
		Logger.debug(this, "TIME TO PRINT DECRYPTED DATA!!!");
		for (Object datum : _data){
			Logger.log(datum.toString());
		}
		Logger.debug(this, "DID IT PRINT ANYTHING?!?!");
	}
	
	/** Nullifies data in container*/
	public void destroyData() {
		for (PwData data : _data) {
			data.setPlatform(new String(new char[data.getPlatform().length()]));
			data.setPlatform(null);
			data.setLogin(new String(new char[data.getLogin().length()]));
			data.setLogin(null);
			data.setPass(new String(new char[data.getPass().length()]));
			data.setPass(null);
		}
	}

//	public String getDataFileName(){
//		return _dataFileName;
//	}
//
//	public boolean dataExists() {
//		Logger.debug(this, "dataExists");
//		return FileManager.getInstance().fileExists(_dataFileName);
//	}

	public void createFileIfNotExist() throws IOException {
		FileManager.getInstance().createFileIfNotExist(_dataFileName);
	}
	// somehow take byte array and parse to JSON file.
	// take JSON file convert to byte array.
}
