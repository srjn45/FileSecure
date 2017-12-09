package code.srjn.filesecure.bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import code.srjn.filesecure.secure.FileSecure;

public class Record implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String RECORD_FILE = "records.ser";

	int id;
	String originalFileName;
	String encryptedFileName;

	FileSecure secure;

	public Record() {
	}

	public Record(String originalFileName, String encryptedFileName, FileSecure secure) {
		this.originalFileName = originalFileName;
		this.encryptedFileName = encryptedFileName;
		this.secure = secure;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getEncryptedFileName() {
		return encryptedFileName;
	}

	public void setEncryptedFileName(String encryptedFileName) {
		this.encryptedFileName = encryptedFileName;
	}

	public FileSecure getSecure() {
		return secure;
	}

	public void setSecure(FileSecure secure) {
		this.secure = secure;
	}

	public static List<Record> readRecords() throws IOException, ClassNotFoundException {
		File file = new File(RECORD_FILE);
		if (!file.createNewFile() && file.length() > 0) {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(RECORD_FILE)));
			List<Record> records = (List<Record>) in.readObject();
			in.close();
			return records;
		}
		return new ArrayList<Record>();
	}

	public static void writeRecords(List<Record> records) throws IOException {
		int id = 0;
		for (Record r : records) {
			r.setId(++id);
		}
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(RECORD_FILE)));
		out.writeObject(records);
		out.close();
	}
}
