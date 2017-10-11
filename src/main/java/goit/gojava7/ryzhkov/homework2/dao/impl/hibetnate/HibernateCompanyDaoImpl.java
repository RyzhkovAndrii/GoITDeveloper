package goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate;

import goit.gojava7.ryzhkov.homework2.dao.interfaces.CompanyDao;
import goit.gojava7.ryzhkov.homework2.model.Company;

public class HibernateCompanyDaoImpl extends HibernateDao<Company, Integer> implements CompanyDao {

    public HibernateCompanyDaoImpl() {
        setClass(Company.class);
    }

}
