package model;

public class Clas {
    private int id;
    private String name;
    private int facultyId;

    public Clas(String name, int facultyId) {
        this.name = name;
        this.facultyId = facultyId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFacultyId() {
        return facultyId;
    }
}
