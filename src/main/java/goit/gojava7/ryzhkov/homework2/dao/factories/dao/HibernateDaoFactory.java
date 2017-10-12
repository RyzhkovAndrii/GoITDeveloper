package goit.gojava7.ryzhkov.homework2.dao.factories.dao;

import goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate.*;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.*;

public class HibernateDaoFactory implements DaoFactory {

    @Override
    public SkillDao getSkillDao() {
        return new HibernateSkillDaoImpl();
    }

    @Override
    public DeveloperDao getDeveloperDao() {
        return new HibernateDeveloperDaoImpl();
    }

    @Override
    public ProjectDao getProjectDao() {
        return new HibernateProjectDaoImpl();
    }

    @Override
    public CompanyDao getCompanyDao() {
        return new HibernateCompanyDaoImpl();
    }

    @Override
    public CustomerDao getCustomerDao() {
        return new HibernateCustomerDaoImpl();
    }
}
