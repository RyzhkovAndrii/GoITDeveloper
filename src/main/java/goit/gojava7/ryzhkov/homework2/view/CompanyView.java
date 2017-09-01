package goit.gojava7.ryzhkov.homework2.view;

import goit.gojava7.ryzhkov.homework2.model.Company;

import java.util.Collection;

public interface CompanyView {

    int getId();

    String getName();

    Collection<Integer> getProjectsId();

    void write(Company company);

}
