package code.srjn.filesecure.secure.impl;

import code.srjn.filesecure.secure.FileSecure;

public class RevertFileSecure implements FileSecure {

	public byte[] secure(byte bytes[]) {

		int l = bytes.length - 1;
		for (int i = 0; i < bytes.length / 2; i++) {
			byte tmp = bytes[i];
			bytes[i] = bytes[l - i];
			bytes[l - i] = tmp;
		}
		return bytes;
	}

	@Override
	public byte[] encrypt(byte[] bytes) {
		return secure(bytes);
	}

	@Override
	public byte[] decrypt(byte[] bytes) {
		return secure(bytes);
	}

}
