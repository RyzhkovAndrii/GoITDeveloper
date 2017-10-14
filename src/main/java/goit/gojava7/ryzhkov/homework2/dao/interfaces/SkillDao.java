package goit.gojava7.ryzhkov.homework2.dao.interfaces;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.util.Collection;

public interface SkillDao extends GenericDao<Skill, Integer> {

    Collection<Skill> getByIdRange(Collection<Integer> idRange) throws StorageException;

}
