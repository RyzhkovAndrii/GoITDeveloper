package goit.gojava7.ryzhkov.homework2.dao.interfaces;

import java.sql.SQLException;
import java.util.Collection;

public interface GenericDao<T, ID> {

    ID save(T entity) throws SQLException;

    T getById(ID id) throws SQLException;

    Collection<T> getAll() throws SQLException;

    void update(T entity) throws SQLException;

    void remove(T entity) throws SQLException;

}