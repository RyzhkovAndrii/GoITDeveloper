package goit.gojava7.ryzhkov.homework2.dao;

import java.util.Collection;

public interface GenericDAO<T, ID> {

    void save(T entity);

    T getById(ID id);

    Collection<T> getAll();

    void update(T entity);

    void remove(T entity);

    void removeAll();

}
