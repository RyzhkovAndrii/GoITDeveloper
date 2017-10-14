package goit.gojava7.ryzhkov.homework2.controller.interfaces;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.model.Developer;

import java.util.Collection;

public interface DeveloperController extends GenericController<Developer, Integer> {

    Collection<Developer> getByIdRange(Collection<Integer> idCollection) throws StorageException;

}
