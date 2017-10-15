package goit.gojava7.ryzhkov.homework2.dao;

import goit.gojava7.ryzhkov.homework2.dao.factories.connection.ConnectionFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.connection.JdbcSimpleConnFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

public class StorageUtils {

    private static Connection jdbcConnection;
    private static ConnectionFactory connectionFactory = new JdbcSimpleConnFactory();
    private static SessionFactory sessionFactory;

    private StorageUtils() {
    }

    public static Connection getJdbcConnection() {
        if (jdbcConnection == null) {
            try {
                jdbcConnection = connectionFactory.getConnection();
            } catch (SQLException e) {
                System.out.printf("Error. Can't connecting to DB!!!");
            }
        }
        return jdbcConnection;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void closeAll() {
        if (jdbcConnection != null) {
            try {
                jdbcConnection.close();
            } catch (SQLException ignored) {
            }
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

}
