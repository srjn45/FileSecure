package code.srjn.filesecure.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static List<File> listFiles(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		List<File> files = new ArrayList<File>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				Pattern pattern = Pattern.compile("^(.*\\.(?!(jar|encrypt|ser)$))?[^.]*$");
				Matcher matcher = pattern.matcher(listOfFiles[i].getName());
				if (matcher.matches()) {
					files.add(listOfFiles[i]);
				}
			} else if (listOfFiles[i].isDirectory()) {
				if (!listOfFiles[i].getName().equals("secure")) {
					files.addAll(listFiles(folder.getPath() + File.separator + listOfFiles[i].getName()));
				}
			}
		}
		return files;
	}

	public static List<FileWrapper> getFileWrappers() {
		List<File> files = listFiles("." + File.separator);

		List<FileWrapper> fileWrappers = new ArrayList<FileWrapper>();
		int id = 0;
		for (File file : files) {
			fileWrappers.add(new FileWrapper(++id, file));
		}
		return fileWrappers;
	}
}
