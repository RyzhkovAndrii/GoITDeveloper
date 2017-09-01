package goit.gojava7.ryzhkov.homework2.dao.factory;

import goit.gojava7.ryzhkov.homework2.dao.*;

interface DaoFactory {

    SkillDao getSkillDao();

    DeveloperDao getDeveloperDao();

    ProjectDao getProjectDao();

    CompanyDao getCompanyDao();

    CustomerDao getCustomerDao();

}
