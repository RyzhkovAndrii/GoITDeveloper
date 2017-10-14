package goit.gojava7.ryzhkov.homework2.dao.interfaces;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;

import java.util.Collection;

public interface GenericDao<T, ID> {

    ID save(T entity) throws StorageException;

    T getById(ID id) throws StorageException;

    Collection<T> getAll() throws StorageException;

    void update(T entity) throws StorageException;

    void remove(T entity) throws StorageException;

}
