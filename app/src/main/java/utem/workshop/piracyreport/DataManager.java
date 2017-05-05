package utem.workshop.piracyreport;

public interface DataManager {
    void saveEmail(String email);

    void saveName(String name);

    void saveCategory(String category);

    void saveBrand(String brand);

    void saveDesc(String description);

    void saveAddr(String addr);

    void saveState(String state);

    void saveLat(double latitude);

    void saveLon(double longitude);

    String getEmail();

    String getName();

    String getCategory();

    String getBrand();

    String getDesc();

    String getState();

    String getAddr();

    double getLat();

    double getLon();
}
