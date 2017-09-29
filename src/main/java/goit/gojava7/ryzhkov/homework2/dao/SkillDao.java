package goit.gojava7.ryzhkov.homework2.dao;

import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.SQLException;
import java.util.Collection;

public interface SkillDao extends GenericDao<Skill, Integer> {

    Collection<Skill> getByDeveloper(Developer developer) throws SQLException;

}
