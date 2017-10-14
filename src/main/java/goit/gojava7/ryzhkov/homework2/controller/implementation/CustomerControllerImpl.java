package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.CustomerController;
import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactoryFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.HibernateDaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.CustomerDao;
import goit.gojava7.ryzhkov.homework2.model.Customer;

import java.util.Collection;

public class CustomerControllerImpl implements CustomerController {

    private DaoFactory daoFactory = DaoFactoryFactory.getDaoFactory();
    private CustomerDao customerDao = daoFactory.getCustomerDao();

    @Override
    public Integer save(Customer customer) throws StorageException {
        return customerDao.save(customer);
    }

    @Override
    public Customer getById(Integer id) throws StorageException {
        return customerDao.getById(id);
    }

    @Override
    public Collection<Customer> getAll() throws StorageException {
        return customerDao.getAll();
    }

    @Override
    public void update(Customer customer) throws StorageException {
        customerDao.update(customer);
    }

    @Override
    public void remove(Customer customer) throws StorageException {
        customerDao.remove(customer);
    }

}
