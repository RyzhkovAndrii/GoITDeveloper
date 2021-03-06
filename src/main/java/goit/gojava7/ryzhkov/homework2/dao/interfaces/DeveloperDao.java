package goit.gojava7.ryzhkov.homework2.dao.interfaces;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.model.Developer;

import java.util.Collection;

public interface DeveloperDao extends GenericDao<Developer, Integer> {

    Collection<Developer> getByIdRange(Collection<Integer> idRange) throws StorageException;

}
