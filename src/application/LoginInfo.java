package application;

import java.time.LocalDate;  

public class LoginInfo {
	private String _platform; // name of service/app
	private String _user; // username
	private String _pass; // password
	private LocalDate _entered; // date entered or updated
	
	public LoginInfo() {
		init();
	}
	
	public LoginInfo(String plat, String usr, String pw) {
		init();
		_platform = plat;
		_user = usr;
		_pass = pw;
	}
	
	private void init() {
		_platform = new String();
		_user = new String();
		_pass = new String();
		_entered = LocalDate.now();		
	}

	public String platform() {
		return _platform;
	}
	
	public String user() {
		return _user;
	}
	
	public String password() {
		return _pass;
	}
	
	public void platform(String plat) {
		_platform = plat;
	}
	
	public void user(String usr) {
		updated();
		_user = usr;
	}
	
	public void password(String pw)	{
		updated();
		_pass = pw;
	}
	
	private void updated() {
		if (_entered != LocalDate.now())
			_entered = LocalDate.now();
	}
}
