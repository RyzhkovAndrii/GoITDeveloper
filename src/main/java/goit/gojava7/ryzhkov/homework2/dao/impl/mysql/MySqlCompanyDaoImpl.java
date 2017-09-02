package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.dao.CompanyDao;
import goit.gojava7.ryzhkov.homework2.model.Company;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.utils.ConnectionUtils;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class MySqlCompanyDaoImpl implements CompanyDao {

    private Connection connection;
    private boolean oldAutoCommitState;

    public MySqlCompanyDaoImpl() {
        connection = ConnectionUtils.getConnection();
    }

    private void saveProjectsOfCompany(Company company) throws SQLException {
        String sql = "INSERT INTO companies_projects(company_id, project_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, company.getId());
            for (Project project : company.getProjects()) {
                pstmt.setInt(2, project.getId());
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving projects of company failed.");
                }
            }
        }
    }

    private void removeProjectsOfCompany(int companyId) throws SQLException {
        String sqlGetCount = "SELECT COUNT(company_id) FROM companies_projects WHERE company_id = ?";
        String sqlDelete = "DELETE FROM companies_projects WHERE company_id = ?";
        try (PreparedStatement pstmtGetCount = connection.prepareStatement(sqlGetCount);
             PreparedStatement pstmtDelete = connection.prepareStatement(sqlDelete)) {
            pstmtGetCount.setInt(1, companyId);
            ResultSet resultSet = pstmtGetCount.executeQuery();
            resultSet.next();
            int projectCount = resultSet.getInt(1);
            pstmtDelete.setInt(1, companyId);
            if (pstmtDelete.executeUpdate() != projectCount) {
                throw new SQLException("Deleting projects of company failed.");
            }
        }
    }

    private void updateProjectsOfCompany(Company company) throws SQLException {
        try {
            removeProjectsOfCompany(company.getId());
            saveProjectsOfCompany(company);
        } catch (SQLException e) {
            throw new SQLException("Updating projects of company failed.", e);
        }

    }

    private int saveBasicInfoOfCompany(Company company) throws SQLException {
        String sql = "INSERT INTO companies(company_name) VALUES (?)";
        try (PreparedStatement pstmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, company.getName());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving company failed, no rows affected.");
            }
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Saving company failed, no ID obtained.");
            }
            return generatedKeys.getInt(1);
        }
    }

    private void updateBasicInfoOfCompany(Company company) throws SQLException {
        String sql = "UPDATE companies SET company_name = ? WHERE company_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.setInt(2, company.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Updating company failed, project for update not found.");
            }
        }
    }

    private Collection<Project> getProjectsByCompanyId(int companyId) throws SQLException {
        MySqlProjectDaoImpl projectDao = new MySqlProjectDaoImpl();
        Collection<Project> projects = new HashSet<>();
        String sql = "SELECT project_id, project_name, project_cost" +
                " FROM companies_projects cp" +
                " JOIN projects USING (project_id)" +
                " WHERE cp.company_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Project project = projectDao.getProjectFromResultSetCurrentRow(rs);
                projects.add(project);
            }
        }
        return projects;
    }

    Company getCompanyFromResultSetCurrentRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("company_id");
        String name = rs.getString("company_name");
        Collection<Project> projects = getProjectsByCompanyId(id);
        return new Company(id, name, projects);
    }

    @Override
    public Integer save(Company company) throws SQLException {
        int id;
        try {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            id = saveBasicInfoOfCompany(company);
            company.setId(id);
            saveProjectsOfCompany(company);
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
    public Company getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM companies WHERE company_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Getting company failed, no ID found.");
            }
            Company company = getCompanyFromResultSetCurrentRow(rs);
            connection.commit();
            return company;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Company> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        String idRange = idCollection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",","(",")"));
        String sql = "SELECT * FROM companies WHERE company_id IN " + idRange;
        Collection<Company> companies = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Company company = getCompanyFromResultSetCurrentRow(rs);
                companies.add(company);
            }
            connection.commit();
            return companies;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Company> getAll() throws SQLException {
        Collection<Company> companies = new LinkedHashSet<>();
        String sql = "SELECT * FROM companies";
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Company company = getCompanyFromResultSetCurrentRow(rs);
                companies.add(company);
            }
            connection.commit();
            return companies;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void update(Company company) throws SQLException {
        oldAutoCommitState = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            updateBasicInfoOfCompany(company);
            updateProjectsOfCompany(company);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void remove(Company company) throws SQLException {
        String sql = "DELETE FROM companies WHERE company_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, company.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Deleting company failed, no rows affected.");
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
