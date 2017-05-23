package utem.workshop.piracyreport.models;


public class User {

    String staffUID;
    String staffName;
    boolean isAdmin;
    boolean keepSync;

    public User(String staffName) {
        this.staffName = staffName;
    }

    public User() {

    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getStaffUID() {
        return staffUID;
    }

    public void setStaffUID(String staffUID) {
        this.staffUID = staffUID;
    }

    public boolean isKeepSync() {
        return keepSync;
    }

    public void setKeepSync(boolean keepSync) {
        this.keepSync = keepSync;
    }
}
