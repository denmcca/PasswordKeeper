package components;

import utils.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
	}
	
	/** Constructor with full parameters */
	public PwData(String platform, String login, String password)
	{
		Logger.debug(this, "constructor 2");
		_platform = platform;
		_login = login;
		_pass = password;
		setDateUpdated();
	}
	
	/** Accepts string to assign name of setPlatform associated to account info */
	public void setPlatform(String platform) {
		_platform = platform;
		setDateUpdated();
	}
	
	/** Accepts string to assign getLogin/username info to account */
	public void setLogin(String login) {
		_login = login;
		setDateUpdated();
	}
	
	/** Accepts string to assign password to account */
	public void setPass(String password) {
		_pass = password;
		setDateUpdated();
	}
	
	/** Assigns current time and date to field 
	 * which keeps track of time and date account info
	 * last updated */
	private void setDateUpdated() {
		Logger.debug(this, "setDateUpdated");
		_dateUpdated = new Date().toString();
	}
	
	/** Returns string which contains name of setPlatform
	 * associated to account */
	public String getPlatform() {
		return _platform;
	}
	
	/** Returns string which contains getLogin/username
	 * associated to account */
	public String getLogin() {
		return _login;
	}
	
	/** Returns string which contains password to account */
	public String getPass() {
		return _pass;
	}
	
	/** Returns string which contains time and date when 
	 * account info last updated */
	private String getDateUpdated() {
		Logger.debug(this, "getDateUpdated");
		return _dateUpdated;
	}

	public StringProperty platformProperty(){
		return new SimpleStringProperty(_platform);
	}

	public StringProperty loginProperty(){
		return new SimpleStringProperty(_login);
	}

	public StringProperty passProperty(){
		return new SimpleStringProperty(_pass);
	}

	public StringProperty dateUpdatedProperty(){
		return new SimpleStringProperty(_dateUpdated);
	}
	
	/** Returns formatted string which contains data from 
	 * account. */
	public String toString() {
		Logger.debug(this, "toString");
		String string;
		string = "Platform: " + getPlatform();
		string += "\nLogin: " + getLogin();
		string += "\nPass: " + getPass();
		string += "\nUpdated: " + getDateUpdated();
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
		
		if(_platform.equals(data.getPlatform()))
			if (_login.equals(data.getLogin()))
				return _pass.equals(data.getPass());
		return false;
	}
	
	/** Accepts object of same class and compares in order of name 
	 * of setPlatform, getLogin in info, then date updated. Returns
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