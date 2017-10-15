package goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate;

import goit.gojava7.ryzhkov.homework2.dao.StorageUtils;
import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.Callable;

public abstract class HibernateDao<T, ID extends Serializable> {

    private static SessionFactory sessionFactory = StorageUtils.getSessionFactory();
    private Session currentSession;
    private Transaction currentTransaction;

    private Class<T> clazz;

    private Session openSession() {
        currentSession = sessionFactory.openSession();
        return currentSession;
    }

    private void closeSession() {
        currentSession.close();
    }

    private Session openSessionWithTransaction() {
        currentSession = sessionFactory.openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    private void closeSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private  <V> V doInSession(Callable<V> actions, String errorMessage) throws StorageException {
        V result;
        try {
            openSession();
            result = actions.call();
            closeSession();
            return result;
        } catch (Exception e) {
            throw new StorageException(errorMessage, e);
        }
    }

    private  <V> V doInSessionWithTransaction(Callable<V> actions, String errorMessage) throws StorageException {
        V result;
        try {
            openSessionWithTransaction();
            result = actions.call();
            closeSessionWithTransaction();
            return result;
        } catch (Exception e) {
            throw new StorageException(errorMessage, e);
        }
    }

    protected void setClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ID save(T entity) throws StorageException {
        return (ID) doInSessionWithTransaction(() -> currentSession.save(entity),
                "Saving failed.");
    }

    public T getById(ID id) throws StorageException {
        T entity;
        entity = doInSession(() -> currentSession.get(clazz, id),
                "Getting failed.");
        if (entity == null) {
            throw new StorageException("Getting failed, no ID found.");
        }
        return entity;
    }

    protected Collection<T> getByIdRange(Collection<ID> idRange, String idFieldName) throws StorageException {
        return doInSession(()-> currentSession
                        .createQuery("from " + clazz.getSimpleName()
                                + " where " + idFieldName + " in :idRange", clazz)
                        .setParameter("idRange", idRange)
                        .getResultList(),
                "Getting failed.");
    }

    public Collection<T> getAll() throws StorageException {
        return doInSession(() -> currentSession
                .createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList(),
                "Getting failed.");
    }

    public void update(T entity) throws StorageException {
        doInSessionWithTransaction(() -> {
            currentSession.update(entity);
            return null;
        }, "Updating failed.");
    }

    public void remove(T entity) throws StorageException {
        doInSessionWithTransaction(() -> {
            currentSession.delete(entity);
            return null;
        }, "Removing failed.");
    }

}
