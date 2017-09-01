package goit.gojava7.ryzhkov.homework2.controller.implementation;

import goit.gojava7.ryzhkov.homework2.controller.interfaces.SkillController;
import goit.gojava7.ryzhkov.homework2.dao.SkillDao;
import goit.gojava7.ryzhkov.homework2.dao.factory.DaoFactory;
import goit.gojava7.ryzhkov.homework2.dao.factory.MySqlDaoFactory;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.SQLException;
import java.util.Collection;

public class SkillControllerImpl implements SkillController {

    private DaoFactory daoFactory = new MySqlDaoFactory();
    private SkillDao skillDao = daoFactory.getSkillDao();

    @Override
    public Collection<Skill> getByCollectionId(Collection<Integer> idCollection) throws SQLException {
        return skillDao.getByCollectionId(idCollection);
    }

    @Override
    public Integer save(Skill skill) throws SQLException {
        return skillDao.save(skill);
    }

    @Override
    public Skill getById(Integer id) throws SQLException {
        return skillDao.getById(id);
    }

    @Override
    public Collection<Skill> getAll() throws SQLException {
        return skillDao.getAll();
    }

    @Override
    public void update(Skill skill) throws SQLException {
        skillDao.update(skill);
    }

    @Override
    public void remove(Skill skill) throws SQLException {
        skillDao.remove(skill);
    }

}
