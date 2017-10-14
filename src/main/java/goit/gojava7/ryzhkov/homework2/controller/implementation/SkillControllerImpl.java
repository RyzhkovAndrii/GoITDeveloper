package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.SkillController;
import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.HibernateDaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.SkillDao;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.util.Collection;

public class SkillControllerImpl implements SkillController {

//    private DaoFactory daoFactory = new JdbcDaoFactory();
    private DaoFactory daoFactory = new HibernateDaoFactory();
    private SkillDao skillDao = daoFactory.getSkillDao();

    @Override
    public Collection<Skill> getByIdRange(Collection<Integer> idCollection) throws StorageException {
        return skillDao.getByIdRange(idCollection);
    }

    @Override
    public Integer save(Skill skill) throws StorageException {
        return skillDao.save(skill);
    }

    @Override
    public Skill getById(Integer id) throws StorageException {
        return skillDao.getById(id);
    }

    @Override
    public Collection<Skill> getAll() throws StorageException {
        return skillDao.getAll();
    }

    @Override
    public void update(Skill skill) throws StorageException {
        skillDao.update(skill);
    }

    @Override
    public void remove(Skill skill) throws StorageException {
        skillDao.remove(skill);
    }

}
