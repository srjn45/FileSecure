package code.srjn.filesecure3.menu.impl;

import code.srjn.filesecure3.menu.Menu;
import code.srjn.filesecure3.bean.FileWrapper;

import java.util.List;

public class SecureMenu implements Menu {

    private List<FileWrapper> files;

    public SecureMenu(List<FileWrapper> files) {
        this.files = files;
    }

    @Override
    public void render() {

        System.out.println("-1. Back");
        System.out.println("0. All Files");

        for (FileWrapper file : files) {
            System.out.println(file.getId() + ". " + file.getName());
        }
        System.out.print("> ");
    }
}
