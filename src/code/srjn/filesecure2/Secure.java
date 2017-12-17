package code.srjn.filesecure2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import code.srjn.filesecure2.bean.DirectoryHandler;
import code.srjn.filesecure2.bean.FileHandler;
import code.srjn.filesecure2.bean.FileWrapper;
import code.srjn.filesecure2.bean.Record;
import code.srjn.filesecure2.secure.FileSecure;
import code.srjn.filesecure2.secure.impl.RevertFileSecure;

public class Secure {

	private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 10;

	public static void main(String[] args) {
		String secureDir = "." + File.separator + "secure" + File.separator;
		File folder = new File(secureDir);
		folder.mkdirs();

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("************FILE SECURE*************");
			System.out.println("1. Secure File");
			System.out.println("2. Retrive Secured File");
			System.out.println("3. Retrive and Remove Secured File");
			System.out.println("4. Exit");
			System.out.print("> ");
			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				try {
					List<FileWrapper> files = new DirectoryHandler().getFiles();

					System.out.println("-1. Back");
					System.out.println("0. All Files");

					for (FileWrapper file : files) {
						System.out.println(file.getId() + ". " + file.getName());
					}
					System.out.print("> ");
					int id = sc.nextInt();

					if (id == -1) {
						break;
					}

					FileSecure fileSecure = null;

					System.out.println("-Encryption Type-");
					System.out.println("1. Revert File");
					System.out.print("> ");
					switch (sc.nextInt()) {
					case 1:
					default:
						fileSecure = new RevertFileSecure();
					}

					if (id != 0) {
						String inFile = null;
						for (FileWrapper file : files) {
							if (file.getId() == id) {
								inFile = file.getPath();
							}
						}
						if (inFile == null) {
							System.out.println("File not found.");
							continue;
						}
						String outFile = System.currentTimeMillis() + ".encrypt";
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						encryptFile(inFile, secureDir + outFile, fileSecure);

						List<Record> records = Record.readRecords();
						Record record = new Record(inFile, outFile, fileSecure);
						records.add(record);
						Record.writeRecords(records);

						Files.delete(Paths.get(inFile));
						System.out.println("File Encrypted");
					} else {
						int count = 0;
						List<Record> records = Record.readRecords();
						for (FileWrapper file : files) {
							String inFile = file.getPath();
							String outFile = System.currentTimeMillis() + ".encrypt";
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							try {

								encryptFile(inFile, secureDir + outFile, fileSecure);

								Record record = new Record(inFile, outFile, fileSecure);
								records.add(record);

								Files.delete(Paths.get(inFile));
								System.out.println(++count + " File Encrypted");
							} catch (IOException e) {
								System.err.println(++count + " Error in Encryption");
								System.err.println(inFile);
								System.err.println(outFile);
								e.printStackTrace();
							}

							Record.writeRecords(records);
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 2:
			case 3:
				try {
					List<Record> records = Record.readRecords();

					if (records.isEmpty()) {
						System.out.println("No records found");
						continue;
					}

					System.out.println("0. All Files");

					for (Record record : records) {
						System.out.println(record.getId() + ". " + record.getOriginalFileName());
					}
					System.out.print("> ");
					int id = sc.nextInt();
					if (id != 0) {
						Iterator<Record> itr = records.iterator();
						while (itr.hasNext()) {
							Record record = itr.next();
							if (record.getId() == id) {

								decryptFile(secureDir + record.getEncryptedFileName(), record.getOriginalFileName(),
										record.getSecure());

								if (choice == 3) {
									Files.delete(Paths.get(secureDir + record.getEncryptedFileName()));
									itr.remove();
								}
								System.out.println("File Decrypted and Removed Secured File");
								break;
							}
						}
					} else {
						Iterator<Record> itr = records.iterator();
						int count = 0;
						while (itr.hasNext()) {
							Record record = itr.next();
							try {

								decryptFile(secureDir + record.getEncryptedFileName(), record.getOriginalFileName(),
										record.getSecure());

								if (choice == 3) {
									Files.delete(Paths.get(secureDir + record.getEncryptedFileName()));
									itr.remove();
								}
								System.out.println(++count + " File Decrypted");
							} catch (IOException e) {
								System.err.println(++count + " Error in Decrypting");
								System.err.println(secureDir + record.getEncryptedFileName());
								System.err.println(record.getOriginalFileName());
								e.printStackTrace();
							}
						}
						System.out.println("All Files Decrypted and Removed Secured File");
					}
					Record.writeRecords(records);
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				sc.close();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Input");
			}
		}
	}

	private static void encryptFile(String sourceFilePath, String targetFilePath, FileSecure fileSecure)
			throws IOException {
		FileHandler sourceFile = new FileHandler(sourceFilePath);
		File path = new File(targetFilePath.substring(0, targetFilePath.lastIndexOf(File.separator)));
		path.mkdirs();
		FileHandler targetFile = new FileHandler(targetFilePath, true);

		ByteBuffer byteBuffer;
		while ((byteBuffer = sourceFile.read(MAX_BUFFER_SIZE)) != null) {
			byteBuffer.flip();
			byte bytes[] = new byte[byteBuffer.remaining()];
			byteBuffer.get(bytes);
			targetFile.write(ByteBuffer.wrap(fileSecure.encrypt(bytes)));
		}

		sourceFile.close();
		targetFile.close();
	}

	private static void decryptFile(String sourceFilePath, String targetFilePath, FileSecure fileSecure)
			throws IOException {
		FileHandler sourceFile = new FileHandler(sourceFilePath);
		File path = new File(targetFilePath.substring(0, targetFilePath.lastIndexOf(File.separator)));
		path.mkdirs();
		FileHandler targetFile = new FileHandler(targetFilePath, true);

		ByteBuffer byteBuffer;
		while ((byteBuffer = sourceFile.read(MAX_BUFFER_SIZE)) != null) {
			byteBuffer.flip();
			byte bytes[] = new byte[byteBuffer.remaining()];
			byteBuffer.get(bytes);
			targetFile.write(ByteBuffer.wrap(fileSecure.decrypt(bytes)));
		}

		sourceFile.close();
		targetFile.close();
	}

}
