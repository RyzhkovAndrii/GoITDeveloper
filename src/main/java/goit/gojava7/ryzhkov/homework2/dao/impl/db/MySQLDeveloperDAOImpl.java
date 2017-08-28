package goit.gojava7.ryzhkov.homework2.dao.impl.db;

import goit.gojava7.ryzhkov.homework2.connection.DBConnection;
import goit.gojava7.ryzhkov.homework2.dao.DeveloperDAO;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class MySQLDeveloperDAOImpl implements DeveloperDAO {

    private Connection connection = null;

    public MySQLDeveloperDAOImpl() {
        connection = DBConnection.getInstance();
    }

    private Collection<Skill> getSkillsByDeveloperId(int developerId) throws SQLException {
        MySQLSkillDAOImpl skillDAO = new MySQLSkillDAOImpl();
        Collection<Skill> skills = new HashSet<>();
        String sql = "SELECT skillId, skillName FROM developers_skills ds NATURAL JOIN skills sk" +
                " WHERE ds.developerId = ?";
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

    private void saveDevelopersSkills(Developer developer) throws SQLException {
        String sql = "INSERT INTO developers_skills(developerId, skillId) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, developer.getId());
            for (Skill skill : developer.getSkills()) {
                pstmt.setInt(2, skill.getId());
                pstmt.executeUpdate();
            }
        }
    }

    private void removeDevelopersSkills(int developerId) throws SQLException {
        String sql = "DELETE FROM developers_skills WHERE developerID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, developerId);
            pstmt.executeUpdate();
        }
    }

    private void updateDevelopersSkills(Developer developer) throws SQLException {
        removeDevelopersSkills(developer.getId());
        saveDevelopersSkills(developer);
    }

    private void saveDevelopersBasicInfo(Developer developer) throws SQLException {
        String sql = "INSERT INTO developers(developerFirstName, developerLastName, salary) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement pstmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, developer.getFirstName());
            pstmt.setString(2, developer.getLastName());
            pstmt.setDouble(3, developer.getSalary());
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            generatedKeys.next();
            int developerId = generatedKeys.getInt(1);
            developer.setId(developerId);
        }
    }

    private void updateDevelopersBasicInfo(Developer developer) throws SQLException {
        String sql = "INSERT INTO developers(developerFirstName, developerLastName, salary) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement pstmtDeveloper = connection.prepareStatement(sql)) {
            pstmtDeveloper.setString(1, developer.getFirstName());
            pstmtDeveloper.setString(2, developer.getLastName());
            pstmtDeveloper.setDouble(3, developer.getSalary());
            pstmtDeveloper.executeUpdate();
        }
    }

    Developer getDeveloperFromResultSetCurrentRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("developerId");
        String firstName = rs.getString("developerFirstName");
        String lastName = rs.getString("developerLastName");
        double salary = rs.getDouble("salary");
        Collection<Skill> skills = getSkillsByDeveloperId(id);
        return new Developer(id, firstName, lastName, salary, skills);
    }

    @Override
    public void save(Developer developer) throws SQLException {
        connection.setAutoCommit(false);
        try {
            saveDevelopersBasicInfo(developer);
            saveDevelopersSkills(developer);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. Save error:" + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Developer getById(Integer id) throws SQLException {
        Developer developer;
        String sql = "SELECT * FROM developers WHERE developerId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                developer = getDeveloperFromResultSetCurrentRow(rs);
            } else {
                throw new SQLException("Getting developer failed, no ID found.");
            }
        }
        return developer;
    }

    @Override
    public Collection<Developer> getAll() throws SQLException {
        Collection<Developer> developers = new LinkedHashSet<>();
        String sql = "SELECT * FROM developers";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Developer developer = getDeveloperFromResultSetCurrentRow(rs);
                developers.add(developer);
            }
        }
        return developers;
    }

    @Override
    public void update(Developer developer) throws SQLException {
        connection.setAutoCommit(false);
        try {
            updateDevelopersBasicInfo(developer);
            updateDevelopersSkills(developer);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. Update error:" + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void remove(Developer developer) throws SQLException {
        String sql = "DELETE FROM developers WHERE skillId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, developer.getId());
            pstmt.executeUpdate();
        }
    }

}
