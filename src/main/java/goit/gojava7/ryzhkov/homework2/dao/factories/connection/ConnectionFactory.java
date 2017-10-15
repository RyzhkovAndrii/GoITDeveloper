package goit.gojava7.ryzhkov.homework2.dao.factories.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    Connection getConnection() throws SQLException;

}
