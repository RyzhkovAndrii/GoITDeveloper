package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.dao.DeveloperDao;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Skill;
import goit.gojava7.ryzhkov.homework2.utils.ConnectionUtils;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class MySqlDeveloperDaoImpl implements DeveloperDao {

    private Connection connection;
    private boolean oldAutoCommitState;


    public MySqlDeveloperDaoImpl() {
        connection = ConnectionUtils.getConnection();
    }

    private void saveDevelopersSkills(Developer developer) throws SQLException {
        String sql = "INSERT INTO developers_skills(developer_id, skill_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, developer.getId());
            for (Skill skill : developer.getSkills()) {
                pstmt.setInt(2, skill.getId());
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving developer's skills failed.");
                }
            }
        }
    }

    private void removeDevelopersSkills(int developerId) throws SQLException {
        String sqlGetCount = "SELECT COUNT(developer_id) FROM developers_skills WHERE developer_id = ?";
        String sqlDelete = "DELETE FROM developers_skills WHERE developer_id = ?";
        try (PreparedStatement pstmtGetCount = connection.prepareStatement(sqlGetCount);
             PreparedStatement pstmtDelete = connection.prepareStatement(sqlDelete)) {
            pstmtGetCount.setInt(1, developerId);
            ResultSet resultSet = pstmtGetCount.executeQuery();
            resultSet.next();
            int skillsCount = resultSet.getInt(1);
            pstmtDelete.setInt(1, developerId);
            if (pstmtDelete.executeUpdate() != skillsCount) {
                throw new SQLException("Deleting developer's skills failed.");
            }
        }
    }

    private void updateDevelopersSkills(Developer developer) throws SQLException {
        try {
            removeDevelopersSkills(developer.getId());
            saveDevelopersSkills(developer);
        } catch (SQLException e) {
            throw new SQLException("Updating developer's skills failed.", e);
        }
    }

    private int saveDevelopersBasicInfo(Developer developer) throws SQLException {
        String sql = "INSERT INTO developers(developer_first_name, developer_last_name, developer_salary) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement pstmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, developer.getFirstName());
            pstmt.setString(2, developer.getLastName());
            pstmt.setDouble(3, developer.getSalary());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving developer failed, no rows affected.");
            }
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Saving developer failed, no ID obtained.");
            }
            return generatedKeys.getInt(1);
        }
    }

    private void updateDevelopersBasicInfo(Developer developer) throws SQLException {
        String sql = "UPDATE developers SET developer_first_name = ?," +
                " developer_last_name = ?, developer_salary = ? WHERE developer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, developer.getFirstName());
            pstmt.setString(2, developer.getLastName());
            pstmt.setDouble(3, developer.getSalary());
            pstmt.setInt(4, developer.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Updating developer failed, developer for update not found.");
            }
        }
    }

    private Collection<Skill> getSkillsByDeveloperId(int developerId) throws SQLException {
        MySqlSkillDaoImpl skillDAO = new MySqlSkillDaoImpl();
        Collection<Skill> skills = new HashSet<>();
        String sql = "SELECT skill_id, skill_name FROM developers_skills ds JOIN skills USING (skill_id)" +
                " WHERE ds.developer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, developerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Skill skill = skillDAO.getSkillFromResultSetCurrentRow(rs);
                skills.add(skill);
            }
        }
        return skills;
    }

    Developer getDeveloperFromResultSetCurrentRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("developer_id");
        String firstName = rs.getString("developer_first_name");
        String lastName = rs.getString("developer_last_name");
        double salary = rs.getDouble("developer_salary");
        Collection<Skill> skills = getSkillsByDeveloperId(id);
        return new Developer(id, firstName, lastName, salary, skills);
    }

    @Override
    public Integer save(Developer developer) throws SQLException {
        int id;
        try {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            id = saveDevelopersBasicInfo(developer);
            developer.setId(id);
            saveDevelopersSkills(developer);
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
    public Developer getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM developers WHERE developer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Getting developer failed, no ID found.");
            }
            Developer developer = getDeveloperFromResultSetCurrentRow(rs);
            connection.commit();
            return developer;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Developer> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        String idRange = idCollection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",","(",")"));
        String sql = "SELECT * FROM developers WHERE developer_id IN " + idRange;
        Collection<Developer> developers = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Developer developer = getDeveloperFromResultSetCurrentRow(rs);
                developers.add(developer);
            }
            connection.commit();
            return developers;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Developer> getAll() throws SQLException {
        Collection<Developer> developers = new LinkedHashSet<>();
        String sql = "SELECT * FROM developers";
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Developer developer = getDeveloperFromResultSetCurrentRow(rs);
                developers.add(developer);
            }
            connection.commit();
            return developers;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void update(Developer developer) throws SQLException {
        oldAutoCommitState = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            updateDevelopersBasicInfo(developer);
            updateDevelopersSkills(developer);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void remove(Developer developer) throws SQLException {
        String sql = "DELETE FROM developers WHERE developer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, developer.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Deleting developer failed, no rows affected.");
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