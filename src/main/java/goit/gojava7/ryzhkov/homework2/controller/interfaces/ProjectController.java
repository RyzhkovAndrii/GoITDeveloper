package goit.gojava7.ryzhkov.homework2.controller.interfaces;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.model.Project;

import java.util.Collection;

public interface ProjectController extends GenericController<Project, Integer> {

    Collection<Project> getByIdRange(Collection<Integer> idCollection) throws StorageException;

}
