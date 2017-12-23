package code.srjn.filesecure3.bean;

import java.io.File;

public class FileWrapper {
	int id;

	String name;
	String path;
	File file;

	public FileWrapper() {

	}

	public FileWrapper(int id, File file) {
		super();
		this.id = id;
		this.file = file;
		name = file.getName();
		path = file.getPath();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "FileWrapper [id=" + id + ", name=" + name + ", path=" + path + ", file=" + file + "]";
	}

}
