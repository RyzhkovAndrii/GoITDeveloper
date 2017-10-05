package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.dao.CustomerDao;
import goit.gojava7.ryzhkov.homework2.dao.impl.DbAbstractDao;
import goit.gojava7.ryzhkov.homework2.model.Customer;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

public class MySqlCustomerDaoImpl extends DbAbstractDao<Customer, Integer> implements CustomerDao {

    private static final String SQL_SAVE = "INSERT INTO customers(customer_name) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE customers SET customer_name = ? WHERE customer_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM customers";
    private static final String SQL_GET_BY_ID = "SELECT * FROM customers WHERE customer_id = ?";
    private static final String SQL_GET_BY_ID_RANGE = "SELECT * FROM customers WHERE customer_id IN ";
    private static final String SQL_REMOVE_BY_ID = "DELETE FROM customers WHERE customer_id = ?";
    private static final String SQL_SAVE_LINKS_PROJECTS =
            "INSERT INTO customers_projects(customer_id, project_id) VALUES (?, ?)";
    private static final String SQL_GET_COUNT_LINKS_PROJECTS =
            "SELECT COUNT(customer_id) FROM customers_projects WHERE customer_id = ?";
    private static final String SQL_DELETE_LINKS_PROJECTS =
            "DELETE FROM customers_projects WHERE customer_id = ?";

    @Override
    public Integer save(Customer customer) throws SQLException {
        return save(customer, SQL_SAVE);
    }

    @Override
    public Customer getById(Integer id) throws SQLException {
        return getById(id, SQL_GET_BY_ID);
    }

    @Override
    public Collection<Customer> getByIdRange(Collection<Integer> idRange) throws SQLException {
        return getByIdRange(idRange, SQL_GET_BY_ID_RANGE);
    }

    @Override
    public Collection<Customer> getAll() throws SQLException {
        return getAll(SQL_GET_ALL);
    }

    @Override
    public void update(Customer customer) throws SQLException {
        update(customer.getId(), customer, SQL_UPDATE);
    }

    @Override
    public void remove(Customer customer) throws SQLException {
        removeById(customer.getId(), SQL_REMOVE_BY_ID);
    }

    @Override
    protected Customer readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("customer_id");
        String name = rs.getString("customer_name");
        return new Customer(id, name, null);
    }

    @Override
    protected void prepareToSave(Customer customer, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, customer.getName());
    }

    @Override
    protected void prepareToUpdate(Customer customer, PreparedStatement pstmt) throws SQLException {
        prepareToSave(customer, pstmt);
        pstmt.setInt(2, customer.getId());
    }

    @Override
    protected Integer readIdFromKeyResultSet(ResultSet rs) throws SQLException {
        return rs.getInt(1); // todo transfer to abstract method
    }

    @Override
    protected void enrichWithLinks(Customer customer) throws SQLException {
        customer.setProjects(new MySqlProjectDaoImpl().getByCustomer(customer));
    }

    @Override
    protected void saveLinksInDb(Integer id, Customer customer) throws SQLException {
        Collection projectsIds = customer.getProjects().stream()
                .map(Project::getId)
                .collect(Collectors.toSet());
        saveLinksInDb(id, projectsIds, SQL_SAVE_LINKS_PROJECTS);
    }

    @Override
    protected void removeLinksFromDb(Integer id) throws SQLException {
        removeLinksFromDb(id, SQL_GET_COUNT_LINKS_PROJECTS, SQL_DELETE_LINKS_PROJECTS);
    }

}
