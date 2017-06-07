package dbclient;

import model.Clas;
import model.Enrollment;
import model.Faculty;
import model.Student;
import org.hsqldb.server.Server;
import org.slf4j.LoggerFactory;

import java.sql.*;

/* Resources:
 *  	https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
 *  	https://www.mkyong.com/jdbc/jdbc-preparestatement-example-create-a-table/
*/

public class MyHsqlClient extends Server {

	private static final String DB_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
	private static final String DB_CONNECTION = "jdbc:hsqldb:hsql://127.0.0.1:9001/test-db";
	private static final String DB_USER = "SA";
	private static final String DB_PASSWORD = "";
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(MyHsqlClient.class);
    private static Connection dbConnection;
    private static PreparedStatement preparedStatement;

    public static void createTable(String createTableSQL) throws SQLException {
		try {
			preparedStatement = dbConnection.prepareStatement(createTableSQL);
			preparedStatement.executeUpdate();
			log.info("Table is created!");

		} catch (SQLException e) {
		    log.info("error: creating table failed!");
			log.error(e.getMessage());
		}
	}

    public static void addStudent(Student student) {
        try {
            preparedStatement = dbConnection.prepareStatement("INSERT INTO Student "
                    + "(NAME, SEX, AGE, LEVEL) VALUES("
                    + "'" + student.getName() + "',"
                    + "'" + student.getSex() + "',"
                    + student.getAge() + ","
                    + student.getLevel() + ")");
            preparedStatement.executeUpdate();
            log.info("Added element to DB");
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.info("Failed adding element");
        }
    }

    public void addFaculty(Faculty faculty) {
        try {
            preparedStatement = dbConnection.prepareStatement("INSERT INTO Faculty "
                    + "(NAME) VALUES("
                    + "'" + faculty.getName() + "'"
                    + ")");
            preparedStatement.executeUpdate();
            log.info("Added element to DB");
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.info("Failed adding element");
        }
    }

    public void addClass(Clas clas) {
        try {
            preparedStatement = dbConnection.prepareStatement("INSERT INTO Class "
                    + "(NAME, FACULTY_ID) VALUES("
                    + "'" + clas.getName() + "',"
                    + clas.getFacultyId()
                    + ")");
            preparedStatement.executeUpdate();
            log.info("Added element to DB");
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.info("Failed adding element");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        try {
            preparedStatement = dbConnection.prepareStatement("INSERT INTO Enrollment "
                    + "(CLASS_ID, STUDENT_ID) VALUES("
                    + enrollment.getClassId() + ","
                    + enrollment.getStudentId()
                    + ")");
            preparedStatement.executeUpdate();
            log.info("Added element to DB");
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.info("Failed adding element");
        }
    }

    public static void sendQuery(int queryNumber, String query) {
        try {
            preparedStatement = dbConnection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            String resultLog = "Wynik zapytania " + queryNumber + ":\n";
            while(resultSet.next()) {
                for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    resultLog += resultSet.getMetaData().getColumnLabel(i) + "=" + resultSet.getNString(i) + ", ";
                }
                resultLog += "\n";
            }
            log.info(resultLog);
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.info("Failed to send query");
        }
    }

	public static void getDBConnection() {
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            log.info("Connected to DB");
		} catch (SQLException e) {
            log.error(e.getMessage());
            log.info("DB Connection failed");
		}
	}

	public static void dropTable(String tableName) {
	    try {
	        dbConnection.prepareStatement("DROP table " + tableName).executeUpdate();
	        log.info("Dropped table: " + tableName);
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.info("Failed drop table!");
        }
    }

    public static void closeConnectionAndStatement() {
        try {
			if (preparedStatement != null) {
				preparedStatement.close();
                log.info("preparedStatement closed");
			}
			if (dbConnection != null) {
				dbConnection.close();
				log.info("Connection closed");
			}
		} catch (SQLException e) {
            log.error(e.getMessage());
            log.info("Failed closing connection!");
        }
    }
}
