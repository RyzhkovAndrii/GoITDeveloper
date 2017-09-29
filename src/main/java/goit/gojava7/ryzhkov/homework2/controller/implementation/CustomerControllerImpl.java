package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.CustomerController;
import goit.gojava7.ryzhkov.homework2.dao.CustomerDao;
import goit.gojava7.ryzhkov.homework2.dao.factory.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factory.MySqlDaoFactory;
import goit.gojava7.ryzhkov.homework2.model.Customer;

import java.sql.SQLException;
import java.util.Collection;

public class CustomerControllerImpl implements CustomerController {

    private DaoFactory daoFactory = new MySqlDaoFactory();
    private CustomerDao customerDao = daoFactory.getCustomerDao();

    @Override
    public Integer save(Customer customer) throws SQLException {
        return customerDao.save(customer);
    }

    @Override
    public Customer getById(Integer id) throws SQLException {
        return customerDao.getById(id);
    }

    @Override
    public Collection<Customer> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        return customerDao.getByIds(idCollection);
    }

    @Override
    public Collection<Customer> getAll() throws SQLException {
        return customerDao.getAll();
    }

    @Override
    public void update(Customer customer) throws SQLException {
        customerDao.update(customer);
    }

    @Override
    public void remove(Customer customer) throws SQLException {
        customerDao.remove(customer);
    }

}
