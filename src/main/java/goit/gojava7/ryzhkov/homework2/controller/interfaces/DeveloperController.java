package goit.gojava7.ryzhkov.homework2.controller.interfaces;

import goit.gojava7.ryzhkov.homework2.model.Developer;

import java.sql.SQLException;
import java.util.Collection;

public interface DeveloperController extends GenericController<Developer, Integer> {

    Collection<Developer> getByIdRange(Collection<Integer> idCollection) throws SQLException;

}
