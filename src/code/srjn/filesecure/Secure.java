package code.srjn.filesecure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import code.srjn.filesecure.bean.FileWrapper;
import code.srjn.filesecure.bean.Record;
import code.srjn.filesecure.secure.FileSecure;
import code.srjn.filesecure.secure.impl.RevertFileSecure;
import code.srjn.filesecure.utils.FileUtil;

public class Secure {

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
					List<FileWrapper> files = FileWrapper.getFileWrappers();

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

						FileUtil.write(secureDir + outFile, fileSecure.encrypt(FileUtil.read(inFile)));

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

								FileUtil.write(secureDir + outFile, fileSecure.encrypt(FileUtil.read(inFile)));

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
								FileUtil.write(record.getOriginalFileName(), record.getSecure()
										.encrypt(FileUtil.read(secureDir + record.getEncryptedFileName())));
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
								FileUtil.write(record.getOriginalFileName(), record.getSecure()
										.encrypt(FileUtil.read(secureDir + record.getEncryptedFileName())));
								if (choice == 3) {
									Files.delete(Paths.get(secureDir + record.getEncryptedFileName()));
								}
								System.out.println(++count + " File Decrypted");
							} catch (IOException e) {
								System.err.println(++count + " Error in Decrypting");
								System.err.println(secureDir + record.getEncryptedFileName());
								System.err.println(record.getOriginalFileName());
								e.printStackTrace();
							}
							if (choice == 3) {
								itr.remove();
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

}
