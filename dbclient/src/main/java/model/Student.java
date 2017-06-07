package model;

public class Student {
    private int id;
    private String name;
    private String sex;
    private int age;
    private int level;

    public Student(String name, String sex, int age, int level) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public int getLevel() {
        return level;
    }
}
