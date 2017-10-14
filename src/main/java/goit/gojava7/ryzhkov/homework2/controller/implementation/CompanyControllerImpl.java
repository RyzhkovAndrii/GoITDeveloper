package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.CompanyController;
import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactoryFactory;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.CompanyDao;
import goit.gojava7.ryzhkov.homework2.model.Company;

import java.util.Collection;

public class CompanyControllerImpl implements CompanyController {

    private DaoFactory daoFactory = DaoFactoryFactory.getDaoFactory();
    private CompanyDao companyDao = daoFactory.getCompanyDao();

    @Override
    public Integer save(Company company) throws StorageException {
        return companyDao.save(company);
    }

    @Override
    public Company getById(Integer id) throws StorageException {
        return companyDao.getById(id);
    }

    @Override
    public Collection<Company> getAll() throws StorageException {
        return companyDao.getAll();
    }

    @Override
    public void update(Company company) throws StorageException {
        companyDao.update(company);
    }

    @Override
    public void remove(Company company) throws StorageException {
        companyDao.remove(company);
    }

}
