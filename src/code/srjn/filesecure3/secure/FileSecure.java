package code.srjn.filesecure3.secure;

import java.io.Serializable;

public interface FileSecure extends Serializable {
	byte[] encrypt(byte bytes[]);
	byte[] decrypt(byte bytes[]);
}
