package application;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EncryptedPacket implements Serializable {
	public byte[] _decrypted_data;
	public byte[] _iv;
	
	public EncryptedPacket() {}
	
	public EncryptedPacket(byte[] iv, byte[] data) {
		_iv = iv;
		_decrypted_data = data;
	}
	
	public void setData(byte[] data) {
		_decrypted_data = data;
	}
	
	public void setIV(byte[] iv) {
		_iv = iv;
	}
	
	public byte[] getData() {
		return _decrypted_data;
	}
	
	public byte[] getIV() {
		return _iv;
	}
}
