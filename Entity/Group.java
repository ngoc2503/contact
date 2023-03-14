package Entity;

public class Group {
    private String groupName;

    public Group() {

    }
    public Group(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
    @Override
    public String toString() {
        return String.format("%s\n", groupName);
    }
}
