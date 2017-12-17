package code.srjn.filesecure2.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileHandler {

	private RandomAccessFile randomAccessFile;

	private FileChannel fileChannel;

	public FileHandler(String filePath) throws FileNotFoundException {
		randomAccessFile = new RandomAccessFile(filePath, "rw");
		fileChannel = randomAccessFile.getChannel();
	}

	public FileHandler(String filePath, boolean create) throws IOException {
		new File(filePath).createNewFile();
		randomAccessFile = new RandomAccessFile(filePath, "rw");
		fileChannel = randomAccessFile.getChannel();
	}

	/**
	 * reads the file from current position to the size specified
	 * 
	 * @param size
	 * @return null if EOF else ByteBuffer
	 * @throws IOException
	 */
	public ByteBuffer read(int size) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(size);
		if (fileChannel.read(buffer) != -1) {
			return buffer;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param buffer
	 * @throws IOException
	 */
	public void write(ByteBuffer buffer) throws IOException {
		while (buffer.hasRemaining()) {
			fileChannel.write(buffer);
		}
	}

	public void close() throws IOException {
		fileChannel.close();
		randomAccessFile.close();
	}

}
