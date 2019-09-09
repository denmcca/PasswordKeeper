package managers;

import utils.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileManager {
	private static FileManager instance = null;
	private final String _storageDirPath = "storage/";

	private FileManager() {
		Logger.debug(this, "constructor");
	}
	
	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}
	
	public void init(String fileName) {
		Logger.debug(this, "init");
		createStorageDirIfNotExist(); // if exists, ignores.
	}
	
	private void createStorageDirIfNotExist() {
		Logger.debug(this, "createStorageDirIfNotExist");
		try {
			Files.createDirectories(Paths.get(_storageDirPath));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	void createFileIfNotExist(String fileName) {
		Logger.debug(this, "checkFileExists");
		Logger.debug(this, Paths.get(_storageDirPath + fileName).toAbsolutePath().toString());
		if (fileExists(fileName)) {
			Paths.get(_storageDirPath + fileName);
		}
		else {
			try {
				Files.createFile(Paths.get(_storageDirPath + fileName));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	// Does not create file
	Boolean fileExists(String fileName) {
		Logger.debug(this, "fileExists");
		return Files.exists(Paths.get(_storageDirPath + fileName));
	}
	
	public void writeData(byte[] data, String fileName, Boolean append) {
		Logger.debug(this, "writeData");
		try {
			FileOutputStream _fileOutputStream = new FileOutputStream(_storageDirPath + fileName, append);
			Logger.debug(this, Arrays.toString(data));
			_fileOutputStream.write(data);
			_fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] readData(String fileName) {
		Logger.debug(this, "readData");
		try {
			FileInputStream _fileInputStream = new FileInputStream(_storageDirPath + fileName);
			File file = new File(_storageDirPath + fileName);
			byte[] data = new byte[(int)file.length()/*returns length in bytes*/];
			_fileInputStream.read(data);
			_fileInputStream.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getStorageDirPath(){
		return _storageDirPath;
	}
}
