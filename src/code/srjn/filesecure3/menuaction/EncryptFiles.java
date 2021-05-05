package code.srjn.filesecure3.menuaction;

import code.srjn.filesecure3.bean.FileHandler;
import code.srjn.filesecure3.bean.FileWrapper;
import code.srjn.filesecure3.bean.Record;
import code.srjn.filesecure3.secure.FileSecure;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static code.srjn.filesecure3.Secure.KB;
import static code.srjn.filesecure3.Secure.SECURE_DIR;

public class EncryptFiles {

    private FileSecure fileSecure;

    public EncryptFiles(FileSecure fileSecure) {
        this.fileSecure = fileSecure;
    }

    public void execute(List<FileWrapper> files, int id, char[] key) throws Exception {
        List<String> inFiles;
        if (id == 0) {
            inFiles = files.stream().map(f -> f.getPath()).collect(Collectors.toList());
        } else {
            inFiles = files.stream().filter(f -> f.getId() == id)
                    .map(f -> f.getPath()).collect(Collectors.toList());
        }
        for (String inFile : inFiles) {
            String outFile = System.currentTimeMillis() + ".encrypt";
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            encryptFile(inFile, SECURE_DIR + outFile, key);

            List<Record> records = Record.readRecords();
            Record record = new Record(inFile, outFile, fileSecure, key);
            records.add(record);
            Record.writeRecords(records);

            Files.delete(Paths.get(inFile));
            System.out.println("File Encrypted");
        }
    }

    private void encryptFile(String sourceFilePath, String targetFilePath, char chunks[])
            throws IOException {

        FileHandler sourceFile = new FileHandler(sourceFilePath);
        File path = new File(targetFilePath.substring(0, targetFilePath.lastIndexOf(File.separator)));
        path.mkdirs();
        FileHandler targetFile = new FileHandler(targetFilePath, true);

        ByteBuffer byteBuffer;
        int i = 0;
        while ((byteBuffer = sourceFile.read(KB * (int) chunks[i++])) != null) {
            byteBuffer.flip();
            byte bytes[] = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            targetFile.write(ByteBuffer.wrap(fileSecure.encrypt(bytes)));
            i %= chunks.length;
        }

        sourceFile.close();
        targetFile.close();
    }
}
