package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.utils.ConnectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public abstract class MySqlAbstractDAO<T, ID> {

    private static <V> V doInTransaction(Callable<V> actions) throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        boolean oldAutoCommitState = connection.getAutoCommit();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); // todo make level choice
        try {
            V result = actions.call();
            connection.commit();
            return result;
        } catch (Exception e) {
            connection.rollback();
            throw new SQLException(e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    protected T getById(ID id, String sql) throws SQLException {
        return doInTransaction(() -> {
            T entity;
            try (PreparedStatement pstmt = ConnectionUtils.getConnection().prepareStatement(sql)) {
                pstmt.setObject(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    entity = readFromResultSet(rs);
                    getLinks(entity);
                } else {
                    throw new SQLException("Getting failed, no ID found.");
                }
                return entity;
            } catch (SQLException e) {
                throw new SQLException("Can't execute sql: " + sql + ". ", e);
            }
        });
    }

    protected Collection<T> getByIdRange(Collection<ID> idRange, String sql) throws SQLException {
        String idRangeSql = idRange.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",","(",")"));
        return getAll(sql + idRangeSql);
    }

    protected Collection<T> getAll(String sql) throws SQLException {
        return doInTransaction(() -> getAllWithOutTransaction(sql));
    }

    protected Collection<T> getAllWithOutTransaction(String sql)
            throws SQLException {
        ConnectionUtils.getConnection().setAutoCommit(false);
        try (Statement stmt = ConnectionUtils.getConnection().createStatement()) {
            Collection<T> entityCollection = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                T entity = readFromResultSet(rs);
                getLinks(entity);
                entityCollection.add(entity);
            }
            return entityCollection;
        } catch (SQLException e) {
            throw new SQLException("Can't execute sql: " + sql + ". ", e);
        }
    }

    protected void removeById(ID id, String sql) throws SQLException {
        doInTransaction(() -> {
            try (PreparedStatement pstmt = ConnectionUtils.getConnection().prepareStatement(sql)) {
                pstmt.setObject(1, id);
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Deleting failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new SQLException("Can't execute sql: " + sql + ". ", e);
            }
            return null;
        });
    }

    protected ID save(T entity, String sql) throws SQLException {
        return doInTransaction(() -> {
            try (PreparedStatement pstmt =
                         ConnectionUtils.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                prepareToSave(entity, pstmt);
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving failed, no rows affected.");
                }
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    throw new SQLException("Saving failed, no ID obtained.");
                }
                ID id = readIdFromKeyResultSet(generatedKeys);
                saveLinks(id, entity);
                return id;
            } catch (SQLException e) {
                throw new SQLException("Can't execute sql: " + sql + ". ", e);
            }
        });
    }

    protected void update(ID id, T entity, String sql) throws SQLException {
        doInTransaction(() -> {
            try (PreparedStatement pstmt = ConnectionUtils.getConnection().prepareStatement(sql)) {
                prepareToUpdate(entity, pstmt);
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Updating failed, id for update not found.");
                }
                removeLinks(id);
                saveLinks(id, entity);
                return null;
            } catch (SQLException e) {
                throw new SQLException("Can't execute sql: " + sql + ". ", e);
            }
        });
    }

    protected abstract T readFromResultSet(ResultSet rs) throws SQLException;

    protected abstract void prepareToSave(T entity, PreparedStatement pstmt) throws SQLException;

    protected abstract void prepareToUpdate(T entity, PreparedStatement pstmt) throws SQLException;

    protected abstract ID readIdFromKeyResultSet(ResultSet rs) throws SQLException; // todo transfer to abstract method

    protected abstract void getLinks(T entity) throws SQLException;

    protected abstract void saveLinks(ID id, T entity) throws SQLException;

    protected abstract void removeLinks(ID id) throws SQLException;

    protected abstract int getLinksCount(ID id) throws SQLException;

}
