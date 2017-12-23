package code.srjn.filesecure3.secure.impl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import code.srjn.filesecure3.secure.FileSecure;

public class PrivateKeyFileSecure implements FileSecure {

	private static final long serialVersionUID = 546870620583929107L;

	char[] key;
	byte[] mask;

	public PrivateKeyFileSecure(char[] key) {
		this.key = key;
		this.mask = toBytes(key);
	}

	private byte[] toBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
		return bytes;
	}

	@Override
	public byte[] encrypt(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] += mask[i % mask.length];
		}
		return bytes;
	}

	@Override
	public byte[] decrypt(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] -= mask[i % mask.length];
		}
		return bytes;
	}

}
