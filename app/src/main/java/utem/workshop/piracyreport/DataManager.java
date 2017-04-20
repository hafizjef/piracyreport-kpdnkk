package utem.workshop.piracyreport;

public interface DataManager {
    void saveEmail(String email);
    void saveName(String name);
    void saveCategory(String category);
    void saveCatType(String type);
    void saveDesc(String description);
    void saveLat(int latitude);
    void saveLon(int longitude);


    String getEmail();
    String getName();
    String getCategory();
    String getType();
    String getDesc();
    int getLat();
    int getLon();
}
