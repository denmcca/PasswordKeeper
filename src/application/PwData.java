package application;

import java.io.Serializable;
import java.util.Date;

public class PwData implements Comparable<PwData>, Serializable {
	private String _platform;
	private String _login;
	private String _pass;
	private String _dateUpdated;
	
	/** Parameterless Constructor */
	public PwData() {
		Logger.debug(this, "constructor");
		_platform = new String();
		_login = new String();
		_pass = new String();
		_dateUpdated = new String();
	}
	
	/** Constructor with full parameters */
	public PwData(String platform, String login, String password)
	{
		Logger.debug(this, "constructor 2");
		_platform = new String();
		_login = new String();
		_pass = new String();
		dateUpdated();
	}
	
	/** Accepts string to assign name of platform associated to account info */
	public void platform(String platform) {
		_platform = platform;
		dateUpdated();
	}
	
	/** Accepts string to assign login/username info to account */
	public void login(String login) {
		_login = login;
		dateUpdated();
	}
	
	/** Accepts string to assign password to account */
	public void pass(String password) {
		_pass = password;
		dateUpdated();
	}
	
	/** Assigns current time and date to field 
	 * which keeps track of time and date account info
	 * last updated */
	private void dateUpdated() {
		Logger.debug(this, "dateUpdated");
		_dateUpdated = new Date().toString();
	}
	
	/** Returns string which contains name of platform 
	 * associated to account */
	public String platform() {
		return _platform;
	}
	
	/** Returns string which contains login/username 
	 * associated to account */
	public String login() {
		return _login;
	}
	
	/** Returns string which contains password to account */
	public String pass() {
		return _pass;
	}
	
	/** Returns string which contains time and date when 
	 * account info last updated */
	public String getDateUpdated() {
		Logger.debug(this, "getDateUpdated");
		return _dateUpdated;
	}
	
	/** Returns formatted string which contains data from 
	 * account. */
	public String toString() {
		Logger.debug(this, "toString");
		String string = new String();
		string = "Platform: " + _platform;
		string += "\nLogin: " + _login;
		string += "\nPass: " + _pass;
		string += "\nUpdated: " + _dateUpdated;
		return string;
	}
	
	/** Accepts object to check if object is equal to 
	 * base object of this class. Returns boolean. */
	public boolean equals(Object obj) {
		Logger.debug(this, "equals");
		if (obj == null || !getClass().equals(obj.getClass())) 
			return false;
		if (obj == this) return true;
		
		PwData data = (PwData)obj;
		
		if(_platform.equals(data.platform())) {
		} if (_login.equals(data.login())) {
			} if (_pass.equals(data.pass())) {
					return true;
				} 
		return false;
	}
	
	/** Accepts object of same class and compares in order of name 
	 * of platform, login in info, then date updated. Returns 
	 * according to standard compareTo methods */
	@Override
	public int compareTo(PwData pwData) {
		Logger.debug(this, "compareTo");
		int result;
		if ((result = _platform.compareTo(pwData._platform)) == 0) {
			if ((result = _login.compareTo(pwData._login)) == 0) {
				// _if_ equal zero will return
				return _dateUpdated.compareTo(pwData._dateUpdated); 
			}
		}
		return result; // if not equal integer > or < zero will be return here
	}
}
