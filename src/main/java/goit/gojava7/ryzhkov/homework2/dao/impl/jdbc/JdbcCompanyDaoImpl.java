package goit.gojava7.ryzhkov.homework2.dao.impl.jdbc;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.CompanyDao;
import goit.gojava7.ryzhkov.homework2.model.Company;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

public class JdbcCompanyDaoImpl extends JdbcAbstractDao<Company, Integer> implements CompanyDao {

    private static final String SQL_SAVE = "INSERT INTO companies(company_name) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE companies SET company_name = ? WHERE company_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM companies";
    private static final String SQL_GET_BY_ID = "SELECT * FROM companies WHERE company_id = ?";
    private static final String SQL_REMOVE_BY_ID = "DELETE FROM companies WHERE company_id = ?";
    private static final String SQL_SAVE_LINKS_PROJECTS =
            "INSERT INTO companies_projects(company_id, project_id) VALUES (?, ?)";
    private static final String SQL_GET_COUNT_LINKS_PROJECTS =
            "SELECT COUNT(company_id) FROM companies_projects WHERE company_id = ?";
    private static final String SQL_DELETE_LINKS_PROJECTS =
            "DELETE FROM companies_projects WHERE company_id = ?";

    @Override
    public Integer save(Company company) throws StorageException {
        return save(company, SQL_SAVE);
    }

    @Override
    public Company getById(Integer id) throws StorageException {
        return getById(id, SQL_GET_BY_ID);
    }

    @Override
    public Collection<Company> getAll() throws StorageException {
        return getAll(SQL_GET_ALL);
    }

    @Override
    public void update(Company company) throws StorageException {
        update(company.getId(), company, SQL_UPDATE);
    }

    @Override
    public void remove(Company company) throws StorageException {
        removeById(company.getId(), SQL_REMOVE_BY_ID);
    }

    @Override
    protected Company readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("company_id");
        String name = rs.getString("company_name");
        return new Company(id, name, null);
    }

    @Override
    protected void prepareToSave(Company company, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, company.getName());
    }

    @Override
    protected void prepareToUpdate(Company company, PreparedStatement pstmt) throws SQLException {
        prepareToSave(company, pstmt);
        pstmt.setInt(2, company.getId());
    }

    @Override
    protected Integer readIdFromKeyResultSet(ResultSet rs) throws SQLException {
        return rs.getInt(1);
    }

    @Override
    protected void enrichWithLinks(Company company) throws SQLException {
        company.setProjects(new JdbcProjectDaoImpl().getByCompany(company));
    }

    @Override
    protected void saveLinksInDb(Integer id, Company company) throws SQLException {
        Collection projectsIds = company.getProjects().stream()
                .map(Project::getId)
                .collect(Collectors.toSet());
        saveLinksInDb(id, projectsIds, SQL_SAVE_LINKS_PROJECTS);
    }

    @Override
    protected void removeLinksFromDb(Integer id) throws SQLException {
        removeLinksFromDb(id, SQL_GET_COUNT_LINKS_PROJECTS, SQL_DELETE_LINKS_PROJECTS);
    }

}
