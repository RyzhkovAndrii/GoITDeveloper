package goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.Callable;

public abstract class HibernateDao<T, ID extends Serializable> {

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory(); //todo remove
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

    private  <V> V doInSession(Callable<V> actions, String errorMessage) throws SQLException {
        V result;
        try {
            openSession();
            result = actions.call();
            closeSession();
            return result;
        } catch (Exception e) {
            throw new SQLException(errorMessage); //todo: StorageException
        }
    }

    private  <V> V doInSessionWithTransaction(Callable<V> actions, String errorMessage) throws SQLException {
        V result;
        try {
            openSessionWithTransaction();
            result = actions.call();
            closeSessionWithTransaction();
            return result;
        } catch (Exception e) {
            throw new SQLException(errorMessage); //todo: StorageException
        }
    }

    protected void setClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ID save(T entity) throws SQLException {
        return (ID) doInSessionWithTransaction(() -> currentSession.save(entity),
                "Saving failed.");
    }

    public T getById(ID id) throws SQLException {
        T entity;
        entity = doInSession(() -> currentSession.get(clazz, id),
                "Getting failed.");
        if (entity == null) {
            throw new SQLException("Getting failed, no ID found."); //todo: StorageException
        }
        return entity;
    }

    protected Collection<T> getByIdRange(Collection<ID> idRange, String idFieldName) throws SQLException {
        return doInSession(()-> currentSession
                        .createQuery("from " + clazz.getSimpleName()
                                + " where " + idFieldName + " in :idRange", clazz) //todo refactoring
                        .setParameter("idRange", idRange)
                        .getResultList(),
                "Getting failed.");
    }

    public Collection<T> getAll() throws SQLException {
        return doInSession(() -> currentSession
                .createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList(),
                "Getting failed.");
    }

    public void update(T entity) throws SQLException {
        doInSessionWithTransaction(() -> {
            currentSession.update(entity);
            return null;
        }, "Updating failed.");
    }

    public void remove(T entity) throws SQLException {
        doInSessionWithTransaction(() -> {
            currentSession.delete(entity);
            return null;
        }, "Removing failed.");
    }

}
