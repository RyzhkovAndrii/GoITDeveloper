package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.ProjectController;
import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.JdbcDaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.ProjectDao;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.util.Collection;

public class ProjectControllerImpl implements ProjectController {

    private DaoFactory daoFactory = new JdbcDaoFactory();
    private ProjectDao projectDao = daoFactory.getProjectDao();

    @Override
    public Integer save(Project project) throws StorageException {
        return projectDao.save(project);
    }

    @Override
    public Project getById(Integer id) throws StorageException {
        return projectDao.getById(id);
    }

    @Override
    public Collection<Project> getByIdRange(Collection<Integer> idCollection) throws StorageException {
        return projectDao.getByIdRange(idCollection);
    }

    @Override
    public Collection<Project> getAll() throws StorageException {
        return projectDao.getAll();
    }

    @Override
    public void update(Project project) throws StorageException {
        projectDao.update(project);
    }

    @Override
    public void remove(Project project) throws StorageException {
        projectDao.remove(project);
    }

}
