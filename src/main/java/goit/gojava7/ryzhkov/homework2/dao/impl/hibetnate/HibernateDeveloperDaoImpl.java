package goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate;

import goit.gojava7.ryzhkov.homework2.dao.interfaces.DeveloperDao;
import goit.gojava7.ryzhkov.homework2.model.Developer;

import java.sql.SQLException;
import java.util.Collection;

public class HibernateDeveloperDaoImpl extends HibernateDao<Developer, Integer> implements DeveloperDao {

    public HibernateDeveloperDaoImpl() {
        setClass(Developer.class);
    }

    @Override
    public Collection<Developer> getByIdRange(Collection<Integer> idRange) throws SQLException {
        return getByIdRange(idRange, "id");
    }
}
