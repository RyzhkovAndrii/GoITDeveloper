package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.ProjectController;
import goit.gojava7.ryzhkov.homework2.dao.ProjectDao;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.MySqlDaoFactory;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.sql.SQLException;
import java.util.Collection;

public class ProjectControllerImpl implements ProjectController {

    private DaoFactory daoFactory = new MySqlDaoFactory();
    private ProjectDao projectDao = daoFactory.getProjectDao();

    @Override
    public Integer save(Project project) throws SQLException {
        return projectDao.save(project);
    }

    @Override
    public Project getById(Integer id) throws SQLException {
        return projectDao.getById(id);
    }

    @Override
    public Collection<Project> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        return projectDao.getByIdRange(idCollection);
    }

    @Override
    public Collection<Project> getAll() throws SQLException {
        return projectDao.getAll();
    }

    @Override
    public void update(Project project) throws SQLException {
        projectDao.update(project);
    }

    @Override
    public void remove(Project project) throws SQLException {
        projectDao.remove(project);
    }

}
