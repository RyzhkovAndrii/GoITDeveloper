package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.dao.ProjectDao;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.utils.ConnectionUtils;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class MySqlProjectDaoImpl implements ProjectDao {

    private Connection connection;
    private boolean oldAutoCommitState;

    public MySqlProjectDaoImpl() {
        connection = ConnectionUtils.getConnection();
    }

    private void saveDevelopersOfProject(Project project) throws SQLException {
        String sql = "INSERT INTO projects_developers(project_id, developer_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, project.getId());
            for (Developer developer : project.getDevelopers()) {
                pstmt.setInt(2, developer.getId());
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving developers of project failed.");
                }
            }
        }
    }

    private void removeDevelopersOfProject(int projectId) throws SQLException {
        String sqlGetCount = "SELECT COUNT(project_id) FROM projects_developers WHERE project_id = ?";
        String sqlDelete = "DELETE FROM projects_developers WHERE project_id = ?";
        try (PreparedStatement pstmtGetCount = connection.prepareStatement(sqlGetCount);
             PreparedStatement pstmtDelete = connection.prepareStatement(sqlDelete)) {
            pstmtGetCount.setInt(1, projectId);
            ResultSet resultSet = pstmtGetCount.executeQuery();
            resultSet.next();
            int developersCount = resultSet.getInt(1);
            pstmtDelete.setInt(1, projectId);
            if (pstmtDelete.executeUpdate() != developersCount) {
                throw new SQLException("Deleting developers of project failed.");
            }
        }
    }

    private void updateDevelopersOfProject(Project project) throws SQLException {
        try {
            removeDevelopersOfProject(project.getId());
            saveDevelopersOfProject(project);
        } catch (SQLException e) {
            throw new SQLException("Updating developers of project failed.", e);
        }

    }

    private int saveBasicInfoOfProject(Project project) throws SQLException {
        String sql = "INSERT INTO projects(project_name) VALUES (?)";
        try (PreparedStatement pstmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, project.getName());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving project failed, no rows affected.");
            }
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Saving project failed, no ID obtained.");
            }
            return generatedKeys.getInt(1);
        }
    }

    private void updateBasicInfoOfProject(Project project) throws SQLException {
        String sql = "UPDATE projects SET project_name = ? WHERE project_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setInt(2, project.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Updating project failed, project for update not found.");
            }
        }
    }

    private Collection<Developer> getDevelopersByProjectId(int projectId) throws SQLException {
        MySqlDeveloperDaoImpl developerDAO = new MySqlDeveloperDaoImpl();
        Collection<Developer> developers = new HashSet<>();
        String sql = "SELECT developer_id, developer_first_name, developer_last_name, salary" +
                " FROM projects_developers pd" +
                " JOIN developers USING (developer_id)" +
                " WHERE ds.project_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Developer developer = developerDAO.getDeveloperFromResultSetCurrentRow(rs);
                developers.add(developer);
            }
        }
        return developers;
    }

    Project getProjectFromResultSetCurrentRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("project_id");
        String name = rs.getString("project_name");
        Collection<Developer> developers = getDevelopersByProjectId(id);
        return new Project(id, name, developers);
    }

    @Override
    public Integer save(Project project) throws SQLException {
        int id;
        try {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            id = saveBasicInfoOfProject(project);
            project.setId(id);
            saveDevelopersOfProject(project);
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
    public Project getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Getting project failed, no ID found.");
            }
            Project project = getProjectFromResultSetCurrentRow(rs);
            connection.commit();
            return project;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Project> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        String idRange = idCollection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",","(",")"));
        String sql = "SELECT * FROM projects WHERE project_id IN " + idRange;
        Collection<Project> projects = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Project project = getProjectFromResultSetCurrentRow(rs);
                projects.add(project);
            }
            connection.commit();
            return projects;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Project> getAll() throws SQLException {
        Collection<Project> projects = new LinkedHashSet<>();
        String sql = "SELECT * FROM projects";
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Project project = getProjectFromResultSetCurrentRow(rs);
                projects.add(project);
            }
            connection.commit();
            return projects;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void update(Project project) throws SQLException {
        oldAutoCommitState = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            updateBasicInfoOfProject(project);
            updateDevelopersOfProject(project);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void remove(Project project) throws SQLException {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, project.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Deleting project failed, no rows affected.");
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
