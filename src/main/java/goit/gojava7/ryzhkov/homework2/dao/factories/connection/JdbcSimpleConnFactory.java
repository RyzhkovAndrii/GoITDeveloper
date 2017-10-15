package goit.gojava7.ryzhkov.homework2.dao.factories.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcSimpleConnFactory implements ConnectionFactory {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/homework1"
            + "?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public JdbcSimpleConnFactory() {
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }

}
