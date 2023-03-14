package Entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;

public class ReadAndWrite {
    public static ObservableList<Contact> loadContact(String path) throws Exception{
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        LineNumberReader reader = new LineNumberReader(new FileReader(path));
        String line = "";
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                String [] str = line.split(":");
                Contact contact = new Contact(str[0].trim(), str[1].trim(), str[2].trim(),
                        str[3].trim(), str[4].trim(),str[5].trim());
                contactList.add(contact);
            }
        }
        reader.close();
        return contactList;
    }
    public static ObservableList<Group> loadGroup(String path) throws Exception{
        ObservableList<Group> groupList = FXCollections.observableArrayList();
        LineNumberReader reader = new LineNumberReader(new FileReader(path));
        String line = "";
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) groupList.add(new Group(line));
        }
        return groupList;
    }
    public static void writeContact(ObservableList<Contact> contacts, String path) throws Exception {
        BufferedWriter wr = new BufferedWriter(new FileWriter(path));
        for (Contact x : contacts) {
            wr.write(x.toString());
        }
        wr.close();
    }
    public static void writeGroup(ObservableList<Group> groupList, String path) throws Exception {
        BufferedWriter wr = new BufferedWriter(new FileWriter(path));
        for (Group x : groupList) {
            wr.write(x.toString());
        }
        wr.close();
    }
 }
