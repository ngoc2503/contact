package dao;

import Entity.Group;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Vector;

public class GroupDAO {
    public List<Group> loadGroup(String fname) throws Exception {
        List<Group> gr = new Vector<>();
        LineNumberReader lnr = new LineNumberReader(new FileReader(fname));
        String line = "";
        while ((line = lnr.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) gr.add(new Group(line));
        }
        lnr.close();
        return gr;
    }
    public int indexOf(List<Group> list, Group group) {
        for (int i = 0; i < list.size(); i++) {
            Group x = list.get(i);
            if (x.getGroupName().equalsIgnoreCase(group.getGroupName())) return i;
        }
        return -1;
    }
    public void saveGroupToList(List<Group> list, Group group){
        list.add(group);
    }
    public List<Group> search(List<Group> list, String search) {
        List<Group> listGr = new Vector<>();
        for(Group x : list) {
            String s = x.getGroupName().toLowerCase();
            if (s.contains(search.toLowerCase())) listGr.add(x);
        }
        return listGr;
    }
    public Boolean updateGroup(List<Group> list, int i, String oldGroup, String newGroup) {
        list.get(i).setGroupName(newGroup);
        int c = 0;
        for (Group x : list) {
            if (x.getGroupName().equalsIgnoreCase(newGroup)) c++;
        }
        if (c >= 2) {
            list.get(i).setGroupName(oldGroup);
            return false;
        }
        return true;
    }
}
