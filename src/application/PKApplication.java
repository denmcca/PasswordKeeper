package application;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

public class PKApplication {
	private EncryptionManager encm;
	private DataManager datam;
	private PasswordManager passm;
	private FileManager filem;
	private InputManager inputm;
	private KeyManager keym;
	private LoginManager logm;
//	private UserInterface ui;
	
	public void start() throws Exception {
		Logger.debug(this, "start");
		
		init();
		boolean loop = true;
		
		if (isFirstTimeLogin()) {
			createPassword();
		} else {
			char choice = 'Y';
			while(!login() & Character.toUpperCase(choice) == 'Y') {
				printLine("Password entered invalid! Try again?");
				promptMessage("Y/N");
				choice = inputm.scanner().next().charAt(0);
			};
		}

		filem.createFileIfNotExist(datam.getDataFileName());
		datam.loadData();
		
		while(loop) {
			int choice; 
			printMainMenu();
			promptMessage("Enter choice");
			choice = InputManager.getInstance().scanner().nextInt();
			
			switch (choice) {
			case 1:
				printPasswordDetails();
				break;
			case 2:
				addCredentials();
				break;
			case 3:
				searchCredentials();
				break;
			default: loop = false;
			}
		}
	}
	
	private void printMainMenu() {
		Logger.debug(this, "printMainMenu");
		printLine("1. Print all credentials");
		printLine("2. Add credential");
		printLine("3. Search credential");
		printLine("9. Exit");
	}
	
	private void searchCredentials() {
		Logger.debug(this, "searchCredentials");
		Logger.log("SEARCH CREDENTIALS");
		String platform = new String();
		inputm.scanner().nextLine();
		this.promptMessage("Enter name of Application/Website");
		platform = inputm.scanner().nextLine();
		Vector<PwData> foundData = datam.findByPlatform(platform);
		for (PwData data : foundData) {
			printLine(data.toString());
		}
	}
	
	private void addCredentials() {
		Logger.debug(this, "addCredentials");
		printLine("ADD CREDENTIAL");
		PwData data = new PwData();
		inputm.scanner().nextLine(); // needed since buffer not clear
		promptMessage("Enter name of application/website");
		data.platform(inputm.scanner().nextLine());
		promptMessage("Enter username");
		data.login(inputm.scanner().nextLine());
		promptMessage("Enter password");
		data.pass(inputm.scanner().nextLine());
		datam.add(data);
	}
	
	private void init() throws Exception {
		Logger.debug(this, "init");
//		ui = new UserInterface();
		encm = EncryptionManager.getInstance();
		datam = DataManager.getInstance();
		passm = PasswordManager.getInstance();
		filem = FileManager.getInstance();
		inputm = InputManager.getInstance();
		keym = KeyManager.getInstance();
		logm = LoginManager.getInstance();
		keym.load();
	}
	
	private void promptMessage(String prompt) {
		Logger.debug(this, "promptMessage");
		System.out.print(prompt + ": ");
	}
	
	private void printLine(String string) {
		Logger.debug(this, "printLine");
		System.out.println(string);
	}
	
	private void printPasswordDetails() {
		Logger.debug(this, "printPasswordDetails");
		for (PwData data : DataManager.getInstance().getAllData()) {
			System.out.println(data.toString());
		}
	}
	
	private Boolean isFirstTimeLogin() throws IOException {
		Logger.debug(this, "isFirstTimeLogin");
		return !PasswordManager.getInstance().passwordHashFileExists();
	}
	
	private Boolean login() throws Exception {
		Logger.debug(this, "login");
		promptMessage("Enter password");
		String pass = InputManager.getInstance().scanner().nextLine();
		passm.load();
		if (PasswordManager.getInstance().verifyPassword(pass.getBytes())) {
			LoginManager.getInstance().sendPasswordToKeyManager(pass.getBytes());
			return true;
		}
		return false;
	}
	
	private void createPassword() throws Exception {
		Logger.debug(this, "createPassword");
		PasswordManager pm = PasswordManager.getInstance();
		promptMessage("First time Login\nEnter password");
		String pass = InputManager.getInstance().scanner().nextLine();
		promptMessage("Enter password again");
		if (pass.equals(InputManager.getInstance().scanner().nextLine())) {
			byte[] hash = pm.generatePasswordHash(pass.getBytes(), keym.getSalt());
			logm.sendPasswordToKeyManager(pass.getBytes());
			PasswordManager.getInstance().setPasswordHash(hash);
		}
	}
	
	private void loadDecryptData() throws Exception {
		Logger.debug(this, "loadDecryptData");
		datam.loadData();
	}
}
