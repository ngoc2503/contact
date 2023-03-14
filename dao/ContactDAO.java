package dao;

import Entity.Contact;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Vector;

public class ContactDAO {
    public List<Contact> loadContact(String fName) throws Exception{
        List<Contact> listCt = new Vector<>();
        LineNumberReader lnr = new LineNumberReader(new FileReader(fName));
        String line = "";
        while ((line = lnr.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                String [] st = line.split(":");
                listCt.add(new Contact(st[0].trim(), st[1].trim(), st[2].trim(),
                        st[3].trim(), st[4].trim(), st[5].trim()));
            }
        }
        lnr.close();
        return listCt;
    }
    public void saveToFile(List<Contact> list, String fName) throws Exception{
        BufferedWriter writer = new BufferedWriter(new FileWriter(fName));
        for (Contact x : list) {
            writer.write(x.toString());
        }writer.close();
    }
    public int indexOf(List<Contact> list, Contact ct) {
        for (int i = 0; i < list.size(); i++) {
            Contact contact = list.get(i);
            if (contact.getFirstName().equalsIgnoreCase(ct.getFirstName()) &&
                    contact.getLastName().equalsIgnoreCase(ct.getLastName())) {
                return i;
            }
        }
        return -1;
    }
    public void seveToList(List<Contact> list, Contact contact) {
        list.add(contact);
    }
    public void updateContact(List<Contact> list, Contact c, int i) {
        Contact x = list.get(i);
        x.setFirstName(c.getFirstName());
        x.setLastName(c.getLastName());
        x.setPhone(c.getPhone());
        x.setEmail(c.getEmail());
        x.setBirthDate(c.getBirthDate());
        x.setGroupName(c.getGroupName());
    }
    public List<Contact> search(List<Contact> list, String group, String search) {
        if (group.equals("All"))group = "";
        List<Contact> listCt = new Vector<>();
        for (Contact x : list) {
            String s = x.toString().toLowerCase();
            if (s.contains(search.toLowerCase()) && x.getGroupName().contains(group))listCt.add(x);
        }
        return listCt;
    }
    public List<Contact> contactbyGroup(List<Contact> list, String group) {
        if (group.equals("All")) return list;
        List<Contact> listCt = new Vector<>();
        for (Contact x : list) {
            String s = x.getGroupName().toLowerCase();
            if(s.contains(group.toLowerCase()))listCt.add(x);
        }
        return listCt;
    }
}
