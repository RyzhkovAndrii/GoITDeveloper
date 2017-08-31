package goit.gojava7.ryzhkov.homework2.dao.impl.db;

import goit.gojava7.ryzhkov.homework2.connection.DBConnection;
import goit.gojava7.ryzhkov.homework2.dao.SkillDAO;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedHashSet;

public class MySQLSkillDAOImpl implements SkillDAO {

    private Connection connection;
    private boolean oldAutoCommitState;

    public MySQLSkillDAOImpl() {
        connection = DBConnection.getInstance();
    }

    protected Skill getSkillFromResultSetCurrentRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("skillId");
        String name = rs.getString("skillName");
        return new Skill(id, name);
    }

    @Override
    public Integer save(Skill skill) throws SQLException {
        String sql = "INSERT INTO skills(skillName) VALUES (?)";
        int id;
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setString(1, skill.getName());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving skill failed, no rows affected.");
            }
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Saving skill failed, no ID obtained.");
            }
            id = generatedKeys.getInt(1);
            connection.commit();
            return id;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Skill getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM skills WHERE skillId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Getting skill failed, no ID found.");
            }
            Skill skill = getSkillFromResultSetCurrentRow(rs);
            connection.commit();
            return skill;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }

    }

    @Override
    public Collection<Skill> getAll() throws SQLException {
        Collection<Skill> skills = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            String sql = "SELECT * FROM skills";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                skills.add(getSkillFromResultSetCurrentRow(rs));
            }
            connection.commit();
            return skills;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void update(Skill skill) throws SQLException {
        String sql = "UPDATE skills SET skillName = ? WHERE skillId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setString(1, skill.getName());
            pstmt.setInt(2, skill.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Updating skill failed, skill for update not found.");
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void remove(Skill skill) throws SQLException {
        String sql = "DELETE FROM skills WHERE skillId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, skill.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Deleting skill failed, no rows affected.");
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

}
