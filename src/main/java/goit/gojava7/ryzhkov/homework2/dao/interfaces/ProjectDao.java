package goit.gojava7.ryzhkov.homework2.dao.interfaces;

import goit.gojava7.ryzhkov.homework2.model.Project;

import java.sql.SQLException;
import java.util.Collection;

public interface ProjectDao extends GenericDao<Project, Integer> {

    Collection<Project> getByIdRange(Collection<Integer> idRange) throws SQLException;

}
