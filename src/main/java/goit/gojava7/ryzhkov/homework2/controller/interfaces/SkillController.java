package goit.gojava7.ryzhkov.homework2.controller.interfaces;

import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.SQLException;
import java.util.Collection;

public interface SkillController extends GenericController<Skill, Integer> {

    Collection<Skill> getByIdRange(Collection<Integer> idCollection) throws SQLException;

}
