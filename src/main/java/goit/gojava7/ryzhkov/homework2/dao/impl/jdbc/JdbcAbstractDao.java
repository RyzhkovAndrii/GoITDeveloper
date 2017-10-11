package goit.gojava7.ryzhkov.homework2.dao.impl.jdbc;

import goit.gojava7.ryzhkov.homework2.dao.ConnectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static goit.gojava7.ryzhkov.homework2.dao.ConnectionUtils.ConnectionFactoryType.SIMPLE;

public abstract class JdbcAbstractDao<T, ID> {

    private Connection connection;

    protected JdbcAbstractDao() {
        ConnectionUtils.setConnectionFactoryType(SIMPLE); // todo change place
        connection = ConnectionUtils.getConnection();
    }

    private <V> V doInTransaction(Callable<V> actions) throws SQLException {
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
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                T entity;
                pstmt.setObject(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    entity = readFromResultSet(rs);
                    enrichWithLinks(entity);
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
        return doInTransaction(() -> getAllWithOutCommit(sql));
    }

    protected Collection<T> getAllWithOutCommit(String sql)
            throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            Collection<T> entityCollection = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                T entity = readFromResultSet(rs);
                enrichWithLinks(entity);
                entityCollection.add(entity);
            }
            return entityCollection;
        } catch (SQLException e) {
            throw new SQLException("Can't execute sql: " + sql + ". ", e);
        }
    }

    protected void removeById(ID id, String sql) throws SQLException {
        doInTransaction(() -> {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
                         connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                prepareToSave(entity, pstmt);
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving failed, no rows affected.");
                }
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    throw new SQLException("Saving failed, no ID obtained.");
                }
                ID id = readIdFromKeyResultSet(generatedKeys);
                saveLinksInDb(id, entity);
                return id;
            } catch (SQLException e) {
                throw new SQLException("Can't execute sql: " + sql + ". ", e);
            }
        });
    }

    protected void update(ID id, T entity, String sql) throws SQLException {
        doInTransaction(() -> {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                prepareToUpdate(entity, pstmt);
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Updating failed, id for update not found.");
                }
                removeLinksFromDb(id);
                saveLinksInDb(id, entity);
                return null;
            } catch (SQLException e) {
                throw new SQLException("Can't execute sql: " + sql + ". ", e);
            }
        });
    }

    protected void saveLinksInDb(ID entityId, Collection linksIds, String sql) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, entityId);
            for (Object linkId: linksIds) {
                pstmt.setObject(2, linkId);
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving links failed.");
                }
            }
        }
    }

    protected void removeLinksFromDb(ID id, String sqlGetLinksCount, String sqlRemove) throws SQLException {
        int linksCount = getLinksCount(id, sqlGetLinksCount);
        try (PreparedStatement pstmt = connection.prepareStatement(sqlRemove)) {
            pstmt.setObject(1, id);
            if (pstmt.executeUpdate() != linksCount) {
                throw new SQLException("Deleting links failed.");
            }
        }
    }

    private int getLinksCount(ID id, String sql) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    protected abstract T readFromResultSet(ResultSet rs) throws SQLException;

    protected abstract void prepareToSave(T entity, PreparedStatement pstmt) throws SQLException;

    protected abstract void prepareToUpdate(T entity, PreparedStatement pstmt) throws SQLException;

    protected abstract ID readIdFromKeyResultSet(ResultSet rs) throws SQLException; // todo transfer to abstract method

    protected abstract void enrichWithLinks(T entity) throws SQLException;

    protected abstract void saveLinksInDb(ID id, T entity) throws SQLException;

    protected abstract void removeLinksFromDb(ID id) throws SQLException;

}