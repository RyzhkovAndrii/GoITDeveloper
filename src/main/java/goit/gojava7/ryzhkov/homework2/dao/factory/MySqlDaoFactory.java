package goit.gojava7.ryzhkov.homework2.dao.factory;

import goit.gojava7.ryzhkov.homework2.dao.*;
import goit.gojava7.ryzhkov.homework2.dao.impl.mysql.*;

public class MySqlDaoFactory implements DaoFactory{

    public MySqlDaoFactory() {
    }

    @Override
    public SkillDao getSkillDao() {
        return new MySqlSkillDaoImpl();
    }

    @Override
    public DeveloperDao getDeveloperDao() {
        return new MySqlDeveloperDaoImpl();
    }

    @Override
    public ProjectDao getProjectDao() {
        return new MySqlProjectDaoImpl();
    }

    @Override
    public CompanyDao getCompanyDao() {
        return new MySqlCompanyDaoImpl();
    }

    @Override
    public CustomerDao getCustomerDao() {
        return new MySqlCustomerDaoImpl();
    }

}
