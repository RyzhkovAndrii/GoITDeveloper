package goit.gojava7.ryzhkov.homework2.dao;

import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.sql.SQLException;
import java.util.Collection;

public interface DeveloperDao extends GenericDao<Developer, Integer> {

    Collection<Developer> getByProject(Project project) throws SQLException;

}
