package code.srjn.filesecure3.menu.impl;

import code.srjn.filesecure3.menu.Menu;
import code.srjn.filesecure3.bean.Record;

import java.util.List;

public class RetrieveMenu implements Menu {

    private List<Record> records;

    public RetrieveMenu(List<Record> records) {
        this.records = records;
    }

    @Override
    public void render() {
        if (records.isEmpty()) {
            System.out.println("No records found");
            return;
        }

        System.out.println("-1. Back");
        System.out.println("0. All Files");

        for (Record record : records) {
            System.out.println(record.getId() + ". " + record.getOriginalFileName());
        }
        System.out.print("> ");
    }
}
