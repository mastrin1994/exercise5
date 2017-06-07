import dbclient.MyHsqlClient;
import model.Clas;
import model.Enrollment;
import model.Faculty;
import model.Student;

import java.sql.SQLException;

public class MyMain {

    public static void main(String[] args) throws SQLException {
        String studentTable = "create table Student "
                + "(STUDENT_ID INTEGER generated BY default AS identity(start WITH 1, increment BY 1), "
                + "NAME varchar(32) NOT NULL, "
				+ "SEX varchar(32) NOT NULL, "
                + "AGE INTEGER NOT NULL,  "
                + "LEVEL INTEGER NOT NULL,  "
				+ "PRIMARY KEY (STUDENT_ID))";
        String facultyTable = "create table Faculty "
                + "(FACULTY_ID INTEGER generated BY default AS identity(start WITH 100, increment BY 1), "
                + "NAME varchar(32) NOT NULL, "
                + "PRIMARY KEY (FACULTY_ID))";
        String classTable = "create table Class "
                + "(CLASS_ID INTEGER generated BY default AS identity(start WITH 1000, increment BY 1), "
                + "NAME varchar(32) NOT NULL, "
                + "FACULTY_ID INTEGER NOT NULL,"
                + "FOREIGN KEY (FACULTY_ID) REFERENCES Faculty (FACULTY_ID), "
                + "PRIMARY KEY (CLASS_ID))";
        String enrollmentTable = "create table Enrollment "
                + "(STUDENT_ID INTEGER NOT NULL,"
				+ "FOREIGN KEY (STUDENT_ID) REFERENCES Student (STUDENT_ID), "
                + "CLASS_ID INTEGER NOT NULL,"
				+ "FOREIGN KEY (CLASS_ID) REFERENCES Class (CLASS_ID))";

        MyHsqlClient client = new MyHsqlClient();
        client.getDBConnection();
        client.createTable(studentTable);
        client.createTable(facultyTable);
        client.createTable(classTable);
        client.createTable(enrollmentTable);

        client.addStudent(new Student("John Smith","male",23,2));
        client.addStudent(new Student("Rebecca Milson","female",27,3));
        client.addStudent(new Student("George Heartbreaker","male",19,1));
        client.addStudent(new Student("Deepika Chopra","female",25,3));

        client.addFaculty(new Faculty("Engineering"));
        client.addFaculty(new Faculty("Philosophy"));
        client.addFaculty(new Faculty("Law and administration"));
        client.addFaculty(new Faculty("Languages"));

        client.addClass(new Clas("Introduction to labour law",102));
        client.addClass(new Clas("Graph algorithms",100));
        client.addClass(new Clas("Existentialism in 20th century",101));
        client.addClass(new Clas("English grammar",103));
        client.addClass(new Clas("From Plato to Kant",101));

        client.addEnrollment(new Enrollment(1, 1000));
        client.addEnrollment(new Enrollment(1, 1002));
        client.addEnrollment(new Enrollment(1, 1003));
        client.addEnrollment(new Enrollment(1, 1004));
            client.addEnrollment(new Enrollment(2, 1002));
            client.addEnrollment(new Enrollment(2, 1003));
        client.addEnrollment(new Enrollment(4, 1000));
        client.addEnrollment(new Enrollment(4, 1002));
        client.addEnrollment(new Enrollment(4, 1003));

        client.sendQuery(1, "SELECT STUDENT_ID, NAME FROM Student");
        client.sendQuery(2, "SELECT STUDENT_ID, NAME FROM Student " +
                "LEFT JOIN Enrollment on Student.STUDENT_ID = Enrollment.STUDENT_ID WHERE Enrollment.STUDENT_ID IS NULL");
        client.sendQuery(3, "SELECT Student.STUDENT_ID, Student.NAME FROM Student " +
                "LEFT JOIN Enrollment on Student.STUDENT_ID = Enrollment.STUDENT_ID WHERE Student.SEX LIKE 'female' " +
                "AND Enrollment.CLASS_ID = 1002");
        client.sendQuery(4, "SELECT Faculty.NAME FROM Faculty " +
                "JOIN Class on Faculty.FACULTY_ID = Class.FACULTY_ID LEFT JOIN Enrollment on Class.CLASS_ID = Enrollment.CLASS_ID " +
                "WHERE Enrollment.CLASS_ID IS NULL");
        client.sendQuery(5, "SELECT MAX(AGE) AS ELDER_STUDENT FROM Student " +
                "LEFT JOIN Enrollment on Student.STUDENT_ID = Enrollment.STUDENT_ID WHERE Enrollment.CLASS_ID = 1000");
        client.sendQuery(6, "SELECT Class.NAME FROM Class, (SELECT CLASS_ID FROM Enrollment GROUP BY CLASS_ID " +
                "HAVING COUNT(*) > 1 ) AS enQuery WHERE Class.CLASS_ID = enQuery.CLASS_ID");
        client.sendQuery(7, "SELECT LEVEL, AVG(AGE) as AVG_AGE FROM Student GROUP BY LEVEL");

        client.dropTable("Enrollment");
        client.dropTable("Class");
        client.dropTable("Faculty");
        client.dropTable("Student");

        client.closeConnectionAndStatement();
    }
}
