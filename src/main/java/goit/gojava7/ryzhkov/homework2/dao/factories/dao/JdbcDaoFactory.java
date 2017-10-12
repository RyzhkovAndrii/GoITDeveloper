package goit.gojava7.ryzhkov.homework2.dao.factories.dao;

import goit.gojava7.ryzhkov.homework2.dao.impl.jdbc.*;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.*;

public class JdbcDaoFactory implements DaoFactory{

    public JdbcDaoFactory() {
    }

    @Override
    public SkillDao getSkillDao() {
        return new JdbcSkillDaoImpl();
    }

    @Override
    public DeveloperDao getDeveloperDao() {
        return new JdbcDeveloperDaoImpl();
    }

    @Override
    public ProjectDao getProjectDao() {
        return new JdbcProjectDaoImpl();
    }

    @Override
    public CompanyDao getCompanyDao() {
        return new JdbcCompanyDaoImpl();
    }

    @Override
    public CustomerDao getCustomerDao() {
        return new JdbcCustomerDaoImpl();
    }

}
