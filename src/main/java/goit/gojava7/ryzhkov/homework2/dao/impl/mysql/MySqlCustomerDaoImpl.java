package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.dao.CustomerDao;
import goit.gojava7.ryzhkov.homework2.model.Customer;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.utils.ConnectionUtils;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class MySqlCustomerDaoImpl implements CustomerDao {

    private Connection connection;
    private boolean oldAutoCommitState;

    public MySqlCustomerDaoImpl() {
        connection = ConnectionUtils.getConnection();
    }

    private void saveProjectsOfCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers_projects(customer_id, project_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getId());
            for (Project project : customer.getProjects()) {
                pstmt.setInt(2, project.getId());
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving projects of customer failed.");
                }
            }
        }
    }

    private void removeProjectsOfCustomer(int customerId) throws SQLException {
        String sqlGetCount = "SELECT COUNT(customer_id) FROM customers_projects WHERE customer_id = ?";
        String sqlDelete = "DELETE FROM customers_projects WHERE customer_id = ?";
        try (PreparedStatement pstmtGetCount = connection.prepareStatement(sqlGetCount);
             PreparedStatement pstmtDelete = connection.prepareStatement(sqlDelete)) {
            pstmtGetCount.setInt(1, customerId);
            ResultSet resultSet = pstmtGetCount.executeQuery();
            resultSet.next();
            int projectCount = resultSet.getInt(1);
            pstmtDelete.setInt(1, customerId);
            if (pstmtDelete.executeUpdate() != projectCount) {
                throw new SQLException("Deleting projects of customer failed.");
            }
        }
    }

    private void updateProjectsOfCustomer(Customer customer) throws SQLException {
        try {
            removeProjectsOfCustomer(customer.getId());
            saveProjectsOfCustomer(customer);
        } catch (SQLException e) {
            throw new SQLException("Updating projects of customer failed.", e);
        }

    }

    private int saveBasicInfoOfCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers(customer_name) VALUES (?)";
        try (PreparedStatement pstmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, customer.getName());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving customer failed, no rows affected.");
            }
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Saving customer failed, no ID obtained.");
            }
            return generatedKeys.getInt(1);
        }
    }

    private void updateBasicInfoOfCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET customer_name = ? WHERE customer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Updating customer failed, project for update not found.");
            }
        }
    }

    private Collection<Project> getProjectsByCustomerId(int customerId) throws SQLException {
        MySqlProjectDaoImpl projectDao = new MySqlProjectDaoImpl();
        Collection<Project> projects = new HashSet<>();
        String sql = "SELECT project_id, project_name" +
                " FROM customers_projects cp" +
                " JOIN projects USING (project_id)" +
                " WHERE cp.customer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Project project = projectDao.getProjectFromResultSetCurrentRow(rs);
                projects.add(project);
            }
        }
        return projects;
    }

    Customer getCustomerFromResultSetCurrentRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("customer_id");
        String name = rs.getString("customer_name");
        Collection<Project> projects = getProjectsByCustomerId(id);
        return new Customer(id, name, projects);
    }

    @Override
    public Integer save(Customer customer) throws SQLException {
        int id;
        try {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            id = saveBasicInfoOfCustomer(customer);
            customer.setId(id);
            saveProjectsOfCustomer(customer);
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
    public Customer getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Getting customer failed, no ID found.");
            }
            Customer customer = getCustomerFromResultSetCurrentRow(rs);
            connection.commit();
            return customer;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Customer> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        String idRange = idCollection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",","(",")"));
        String sql = "SELECT * FROM customers WHERE customer_id IN " + idRange;
        Collection<Customer> customers = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Customer customer = getCustomerFromResultSetCurrentRow(rs);
                customers.add(customer);
            }
            connection.commit();
            return customers;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public Collection<Customer> getAll() throws SQLException {
        Collection<Customer> customers = new LinkedHashSet<>();
        String sql = "SELECT * FROM customers";
        try (Statement stmt = connection.createStatement()) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Customer customer = getCustomerFromResultSetCurrentRow(rs);
                customers.add(customer);
            }
            connection.commit();
            return customers;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void update(Customer customer) throws SQLException {
        oldAutoCommitState = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            updateBasicInfoOfCustomer(customer);
            updateProjectsOfCustomer(customer);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction is being rolled back. " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(oldAutoCommitState);
        }
    }

    @Override
    public void remove(Customer customer) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            oldAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);
            pstmt.setInt(1, customer.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Deleting customer failed, no rows affected.");
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
