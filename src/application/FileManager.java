package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
	private static FileManager _fileManager = null;
	private final String _storageDirPath = "storage/";

	private FileManager() {
		Logger.debug(this, "constructor");
	}
	
	public static FileManager getInstance() {
		if (_fileManager == null) {
			_fileManager = new FileManager();
		}
		return _fileManager;
	}
	
	public void init(String fileName) throws IOException {
		Logger.debug(this, "init");
		createStorageDirIfNotExist(); // if exists, ignores.
	}
	
	public Path createStorageDirIfNotExist() throws IOException {
		Logger.debug(this, "createStorageDirIfNotExist");
		return Files.createDirectories(Paths.get(_storageDirPath));
	}
	
	public Path createFileIfNotExist(String fileName) throws IOException {
		Logger.debug(this, "checkFileExists");
		Logger.debug(this, Paths.get(_storageDirPath + fileName).toAbsolutePath().toString());
		if (fileExists(fileName)) return  Paths.get(_storageDirPath + fileName);
		return Files.createFile(Paths.get(_storageDirPath + fileName));
	}
	
	// Does not create file
	public Boolean fileExists(String fileName) {
		Logger.debug(this, "fileExists");
		return Files.exists(Paths.get(_storageDirPath + fileName));
	}
	
	public void writeData(byte[] data, String fileName, Boolean append) throws IOException {
		Logger.debug(this, "writeData");
		FileOutputStream _fileOutputStream = new FileOutputStream(_storageDirPath + fileName, append);
		_fileOutputStream.write(data);
		_fileOutputStream.close();
	}
	
	public byte[] readData(String fileName) throws IOException {
		Logger.debug(this, "readData");
		FileInputStream _fileInputStream = new FileInputStream(_storageDirPath + fileName);
		File file = new File(_storageDirPath + fileName);
		byte[] data = new byte[(int)file.length()/*returns length in bytes*/];
		_fileInputStream.read(data);
		_fileInputStream.close();
		return data;
	}

	public String getStorageDirPath(){
		return _storageDirPath;
	}
}
