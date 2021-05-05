package code.srjn.filesecure3.menu.impl;

import code.srjn.filesecure3.menu.Menu;

public class MainMenu implements Menu {
    @Override
    public void render() {
        System.out.println("************FILE SECURE*************");
        System.out.println("1. Secure File");
        System.out.println("2. Retrieve Secured File");
        System.out.println("3. Retrieve and Remove Secured File");
        System.out.println("4. Exit");
        System.out.print("> ");
    }
}
