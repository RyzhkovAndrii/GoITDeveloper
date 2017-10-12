package goit.gojava7.ryzhkov.homework2.dao.factories.dao;

import goit.gojava7.ryzhkov.homework2.dao.interfaces.*;

public interface DaoFactory {

    SkillDao getSkillDao();

    DeveloperDao getDeveloperDao();

    ProjectDao getProjectDao();

    CompanyDao getCompanyDao();

    CustomerDao getCustomerDao();

}
