package code.srjn.filesecure.secure;

import java.io.Serializable;

public interface FileSecure extends Serializable {
	public byte[] encrypt(byte bytes[]);

	public byte[] decrypt(byte bytes[]);
}
