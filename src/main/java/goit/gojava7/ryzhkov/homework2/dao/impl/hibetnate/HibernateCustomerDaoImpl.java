package goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate;

import goit.gojava7.ryzhkov.homework2.dao.interfaces.CustomerDao;
import goit.gojava7.ryzhkov.homework2.model.Customer;

public class HibernateCustomerDaoImpl extends HibernateDao<Customer, Integer> implements CustomerDao {

    public HibernateCustomerDaoImpl() {
        setClass(Customer.class);
    }

}
