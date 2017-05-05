package utem.workshop.piracyreport;

/**
 * Created by max on 5/5/17.
 */

public class User {
    public String name;
    public boolean isAdmin;

    public User(String name) {
        this.name = name;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
