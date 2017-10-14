package goit.gojava7.ryzhkov.homework2.dao.impl.hibetnate;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.SkillDao;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.util.Collection;

public class HibernateSkillDaoImpl extends HibernateDao<Skill, Integer> implements SkillDao {

    public HibernateSkillDaoImpl() {
        setClass(Skill.class);
    }

    @Override
    public Collection<Skill> getByIdRange(Collection<Integer> idRange) throws StorageException {
        return getByIdRange(idRange, "id");
    }

}
