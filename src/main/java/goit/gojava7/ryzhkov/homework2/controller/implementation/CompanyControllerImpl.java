package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.CompanyController;
import goit.gojava7.ryzhkov.homework2.dao.CompanyDao;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.MySqlDaoFactory;
import goit.gojava7.ryzhkov.homework2.model.Company;

import java.sql.SQLException;
import java.util.Collection;

public class CompanyControllerImpl implements CompanyController {

    private DaoFactory daoFactory = new MySqlDaoFactory();
    private CompanyDao companyDao = daoFactory.getCompanyDao();

    @Override
    public Integer save(Company company) throws SQLException {
        return companyDao.save(company);
    }

    @Override
    public Company getById(Integer id) throws SQLException {
        return companyDao.getById(id);
    }

    @Override
    public Collection<Company> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        return companyDao.getByIdRange(idCollection);
    }

    @Override
    public Collection<Company> getAll() throws SQLException {
        return companyDao.getAll();
    }

    @Override
    public void update(Company company) throws SQLException {
        companyDao.update(company);
    }

    @Override
    public void remove(Company company) throws SQLException {
        companyDao.remove(company);
    }

}
