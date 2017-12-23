package code.srjn.filesecure3.secure;

import java.io.Serializable;

public interface FileSecure extends Serializable {
	public byte[] encrypt(byte bytes[]);

	public byte[] decrypt(byte bytes[]);
}
