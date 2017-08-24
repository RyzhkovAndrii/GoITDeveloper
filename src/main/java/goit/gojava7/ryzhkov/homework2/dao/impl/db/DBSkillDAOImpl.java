package goit.gojava7.ryzhkov.homework2.dao.impl.db;

import goit.gojava7.ryzhkov.homework2.connection.DBConnection;
import goit.gojava7.ryzhkov.homework2.dao.SkillDAO;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedHashSet;

public class DBSkillDAOImpl implements SkillDAO {

    private Connection connection = null;

    public DBSkillDAOImpl() {
        connection = DBConnection.getInstance();
    }

    @Override
    public void save(Skill skill) throws SQLException {
        String sql = "INSERT INTO skills(skillName) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, skill.getName());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving skill failed, no rows affected.");
            }
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                skill.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Saving skill failed, no ID obtained.");
            }
        }
    }

    @Override
    public Skill getById(Integer id) throws SQLException {
        Skill skill = new Skill();
        String sql = "SELECT * FROM skills WHERE skillId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                skill.setId(id);
                skill.setName(rs.getString(2));
            } else {
                throw new SQLException("Getting skill failed, no ID found.");
            }
        }
        return skill;
    }

    @Override
    public Collection<Skill> getAll() throws SQLException {
        Collection<Skill> skills = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement()) {
            String sql = "SELECT * FROM skills";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                skills.add(new Skill(id, name));
            }
        }
        return skills;
    }

    @Override
    public void update(Skill skill) throws SQLException {
        String sql = "UPDATE skills SET skillName = ? WHERE skillId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, skill.getName());
            pstmt.setInt(2, skill.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Updating skill failed, no rows affected.");
            }
        }
    }

    @Override
    public void remove(Skill skill) throws SQLException {
        String sql = "DELETE FROM skills WHERE skillId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, skill.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Deleting skill failed, no rows affected.");
            }
        }
    }

    @Override
    public void removeAll() throws SQLException {
        int affectedRows;
        String sql = "DELETE FROM skills";
        try (Statement pstmt = connection.createStatement()) {
            affectedRows = pstmt.executeUpdate(sql);
            if (affectedRows == 0) {
                throw new SQLException("Deleting skill failed, no rows affected.");
            }
        }
    }

}
