package goit.gojava7.ryzhkov.homework2.dao.impl.jdbc;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.ProjectDao;
import goit.gojava7.ryzhkov.homework2.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

public class JdbcProjectDaoImpl extends JdbcAbstractDao<Project, Integer> implements ProjectDao {

    private static final String SQL_SAVE =
            "INSERT INTO projects(project_name, project_cost) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE projects SET project_name = ?, project_cost = ?" +
            " WHERE project_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM projects";
    private static final String SQL_GET_BY_ID = "SELECT * FROM projects WHERE project_id = ?";
    private static final String SQL_GET_BY_ID_RANGE = "SELECT * FROM projects WHERE project_id IN ";
    private static final String SQL_GET_BY_COMPANY_ID =
            "SELECT project_id, project_name, project_cost " +
            "FROM companies_projects cp " +
            "JOIN projects USING (project_id) " +
            "WHERE cp.company_id = ";
    private static final String SQL_GET_BY_CUSTOMER_ID =
            "SELECT project_id, project_name, project_cost " +
            "FROM customers_projects cp " +
            "JOIN projects USING (project_id) " +
            "WHERE cp.customer_id = ";
    private static final String SQL_REMOVE_BY_ID = "DELETE FROM projects WHERE project_id = ?";
    private static final String SQL_SAVE_LINKS_DEVELOPERS =
                "INSERT INTO projects_developers(project_id, developer_id) VALUES (?, ?)";
    private static final String SQL_GET_COUNT_LINKS_DEVELOPERS =
            "SELECT COUNT(project_id) FROM projects_developers WHERE project_id = ?";
    private static final String SQL_DELETE_LINKS_DEVELOPERS =
            "DELETE FROM projects_developers WHERE project_id = ?";

    Collection<Project> getByCompany(Company company) throws SQLException {
        return getAllWithOutCommit(SQL_GET_BY_COMPANY_ID + company.getId());
    }

    Collection<Project> getByCustomer(Customer customer) throws SQLException {
        return getAllWithOutCommit(SQL_GET_BY_CUSTOMER_ID + customer.getId());
    }

    @Override
    public Integer save(Project project) throws StorageException {
        return save(project, SQL_SAVE);
    }

    @Override
    public Project getById(Integer id) throws StorageException {
        return getById(id, SQL_GET_BY_ID);
    }

    @Override
    public Collection<Project> getByIdRange(Collection<Integer> idRange) throws StorageException {
        return getByIdRange(idRange, SQL_GET_BY_ID_RANGE);
    }

    @Override
    public Collection<Project> getAll() throws StorageException {
        return getAll(SQL_GET_ALL);
    }

    @Override
    public void update(Project project) throws StorageException {
        update(project.getId(), project, SQL_UPDATE);
    }

    @Override
    public void remove(Project project) throws StorageException {
        removeById(project.getId(), SQL_REMOVE_BY_ID);
    }

    @Override
    protected Project readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("project_id");
        String name = rs.getString("project_name");
        double cost = rs.getDouble("project_cost");
        return new Project(id, name, cost, null);
    }

    @Override
    protected void prepareToSave(Project project, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, project.getName());
        pstmt.setDouble(2, project.getCost());
    }

    @Override
    protected void prepareToUpdate(Project project, PreparedStatement pstmt) throws SQLException {
        prepareToSave(project, pstmt);
        pstmt.setInt(3, project.getId());
    }

    @Override
    protected Integer readIdFromKeyResultSet(ResultSet rs) throws SQLException {
        return rs.getInt(1); // todo transfer to abstract method
    }

    @Override
    protected void enrichWithLinks(Project project) throws SQLException {
        project.setDevelopers(new JdbcDeveloperDaoImpl().getByProject(project));
    }

    @Override
    protected void saveLinksInDb(Integer id, Project project) throws SQLException {
        Collection developersIds = project.getDevelopers().stream()
                .map((developer) -> developer.getId())
                .collect(Collectors.toSet());
        saveLinksInDb(id, developersIds, SQL_SAVE_LINKS_DEVELOPERS);
    }

    @Override
    protected void removeLinksFromDb(Integer id) throws SQLException {
        removeLinksFromDb(id, SQL_GET_COUNT_LINKS_DEVELOPERS, SQL_DELETE_LINKS_DEVELOPERS);
    }

}
