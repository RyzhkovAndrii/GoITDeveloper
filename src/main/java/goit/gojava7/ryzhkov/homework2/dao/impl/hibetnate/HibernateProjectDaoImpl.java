package goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.ProjectDao;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.util.Collection;

public class HibernateProjectDaoImpl extends HibernateDao<Project, Integer> implements ProjectDao {

    public HibernateProjectDaoImpl() {
        setClass(Project.class);
    }

    @Override
    public Collection<Project> getByIdRange(Collection<Integer> idRange) throws StorageException {
        return getByIdRange(idRange, "id");
    }
}
