package code.srjn.filesecure.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static byte[] read(String inFile) throws IOException, ClassNotFoundException {
		File file = new File(inFile);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		byte bytes[] = new byte[(int) file.length()];
		bis.read(bytes);
		bis.close();
		return bytes;
	}

	public static void write(String outFile, byte bytes[]) throws IOException {
		File nf = new File(outFile);
		File path = new File(outFile.substring(0, outFile.lastIndexOf(File.separator)));
		path.mkdirs();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(nf));
		bos.write(bytes);
		bos.flush();
		bos.close();
	}
}
