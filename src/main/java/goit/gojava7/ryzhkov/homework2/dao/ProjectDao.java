package goit.gojava7.ryzhkov.homework2.dao;

import goit.gojava7.ryzhkov.homework2.model.Company;
import goit.gojava7.ryzhkov.homework2.model.Customer;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.sql.SQLException;
import java.util.Collection;

public interface ProjectDao extends GenericDao<Project, Integer> {

    Collection<Project> getByCompany(Company company) throws SQLException;

    Collection<Project> getByCustomer(Customer customer) throws SQLException;

}
