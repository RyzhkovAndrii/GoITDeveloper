package goit.gojava7.ryzhkov.homework2.dao;

import java.sql.SQLException;
import java.util.Collection;

public interface GenericDAO<T, ID> {

    void save(T entity) throws SQLException;

    T getById(ID id) throws SQLException;

    Collection<T> getAll() throws SQLException;

    void update(T entity) throws SQLException;

    void remove(T entity) throws SQLException;

    void removeAll() throws SQLException;

}
