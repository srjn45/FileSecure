package code.srjn.filesecure3.menuaction;

import code.srjn.filesecure3.bean.FileHandler;
import code.srjn.filesecure3.bean.Record;
import code.srjn.filesecure3.secure.FileSecure;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static code.srjn.filesecure3.Secure.KB;
import static code.srjn.filesecure3.Secure.SECURE_DIR;

public class DecryptFiles {

    public void execute(List<Record> records, int id, char[] key, boolean deleteEncryptedFile) throws Exception {
        List<Record> inRecords = records;
        if (id != 0) {
            inRecords = records.stream().filter(r -> r.getId() == id).collect(Collectors.toList());
        }

        Iterator<Record> itr = inRecords.iterator();
        while (itr.hasNext()) {
            Record record = itr.next();
            if (Arrays.equals(record.getKey(), key)) {
                decryptFile(SECURE_DIR + record.getEncryptedFileName(), record.getOriginalFileName(),
                        record.getSecure(), key);

                if (deleteEncryptedFile) {
                    Files.delete(Paths.get(SECURE_DIR + record.getEncryptedFileName()));
                    itr.remove();
                    System.out.println("File Decrypted and Removed Secured File");
                } else {
                    System.out.println("File Decrypted");
                }
            } else {
                System.out.println("Incorrect Key for " + record.getOriginalFileName());
            }
            break;
        }
        Record.writeRecords(inRecords);
    }

    private void decryptFile(String sourceFilePath, String targetFilePath, FileSecure fileSecure, char chunks[])
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
            targetFile.write(ByteBuffer.wrap(fileSecure.decrypt(bytes)));
            i %= chunks.length;
        }

        sourceFile.close();
        targetFile.close();
    }
}
