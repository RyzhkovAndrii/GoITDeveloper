package goit.gojava7.ryzhkov.homework2.dao.factories.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/homework1"
            + "?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    private ConnectionUtils() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD); // todo add connectionFactory
            } catch (SQLException e) {
                e.printStackTrace(); //todo print some message
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

}
