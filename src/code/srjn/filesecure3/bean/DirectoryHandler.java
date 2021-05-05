package code.srjn.filesecure3.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoryHandler {

	String dirPath;

	List<String> excludes = new ArrayList<>();

	public DirectoryHandler() {
		this("." + File.separator);
	}

	public DirectoryHandler(String dirPath) {
		this.dirPath = dirPath;
		excludes.add("jar");
		excludes.add("ser");
		excludes.add("encrypt");
	}

	public DirectoryHandler(String dirPath, List<String> excludes) {
		this.dirPath = dirPath;
		this.excludes = excludes;
	}

	private List<File> listFiles(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		List<File> files = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		sb.append("^(.*\\.(?!(");
		for (String exclude : excludes) {
			sb.append(exclude + "|");
		}
		sb.append("srjn)$))?[^.]*$");

		Pattern pattern = Pattern.compile(sb.toString());

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				Matcher matcher = pattern.matcher(listOfFiles[i].getName());
				if (matcher.matches()) {
					files.add(listOfFiles[i]);
				}
			} else if (listOfFiles[i].isDirectory()) {
				files.addAll(listFiles(folder.getPath() + File.separator + listOfFiles[i].getName()));
			}
		}
		return files;
	}

	public List<FileWrapper> getFiles() {
		List<File> files = listFiles(dirPath);

		List<FileWrapper> fileWrappers = new ArrayList<FileWrapper>();
		int id = 0;
		for (File file : files) {
			fileWrappers.add(new FileWrapper(++id, file));
		}
		return fileWrappers;
	}

}
