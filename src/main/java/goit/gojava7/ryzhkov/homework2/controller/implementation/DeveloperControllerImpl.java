package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.DeveloperController;
import goit.gojava7.ryzhkov.homework2.dao.DeveloperDao;
import goit.gojava7.ryzhkov.homework2.dao.factory.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factory.MySqlDaoFactory;
import goit.gojava7.ryzhkov.homework2.model.Developer;

import java.sql.SQLException;
import java.util.Collection;

public class DeveloperControllerImpl implements DeveloperController {

    private DaoFactory daoFactory = new MySqlDaoFactory();
    private DeveloperDao developerDao = daoFactory.getDeveloperDao();

    @Override
    public Integer save(Developer developer) throws SQLException {
        return developerDao.save(developer);
    }

    @Override
    public Developer getById(Integer id) throws SQLException {
        return developerDao.getById(id);
    }

    @Override
    public Collection<Developer> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        return developerDao.getByCollectionId(idCollection);
    }

    @Override
    public Collection<Developer> getAll() throws SQLException {
        return developerDao.getAll();
    }

    @Override
    public void update(Developer developer) throws SQLException {
        developerDao.update(developer);
    }

    @Override
    public void remove(Developer developer) throws SQLException {
        developerDao.remove(developer);
    }

}
