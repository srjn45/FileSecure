package code.srjn.filesecure3;

import code.srjn.filesecure3.menuaction.DecryptFiles;
import code.srjn.filesecure3.menuaction.EncryptFiles;
import code.srjn.filesecure3.menu.Menu;
import code.srjn.filesecure3.menu.impl.MainMenu;
import code.srjn.filesecure3.menu.impl.RetrieveMenu;
import code.srjn.filesecure3.menu.impl.SecureMenu;
import code.srjn.filesecure3.bean.DirectoryHandler;
import code.srjn.filesecure3.bean.FileWrapper;
import code.srjn.filesecure3.bean.Record;
import code.srjn.filesecure3.secure.FileSecure;
import code.srjn.filesecure3.secure.impl.RevertFileSecure;

import java.io.Console;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Secure {

    // private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 10;
    public static final int KB = 1024;
    // private static final int MB = 1024 * 1024;
    public static String SECURE_DIR = "." + File.separator + "secure" + File.separator;

    public static void main(String[] args) {
        File folder = new File(SECURE_DIR);
        folder.mkdirs();

        Scanner sc = new Scanner(System.in);

        Menu mainMenu = new MainMenu();
        while (true) {
            mainMenu.render();
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    try {
                        List<FileWrapper> files = new DirectoryHandler().getFiles();
                        new SecureMenu(files).render();
                        int id = sc.nextInt();
                        if (id == -1) {
                            break;
                        }
                        FileSecure fileSecure = new RevertFileSecure();
                        char key[] = getKey();
                        new EncryptFiles(fileSecure).execute(files, id, key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                case 3:
                    try {
                        List<Record> records = Record.readRecords();
                        new RetrieveMenu(records).render();
                        int id = sc.nextInt();
                        if (id == -1) {
                            break;
                        }
                        char key[] = getKey();
                        new DecryptFiles().execute(records, id, key, choice == 3);
                    } catch (Exception e) {
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

    private static char[] getKey() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Console not available");
            System.exit(0);
        }
        System.out.print("Enter Secret key : ");
        return console.readPassword();
    }

}
