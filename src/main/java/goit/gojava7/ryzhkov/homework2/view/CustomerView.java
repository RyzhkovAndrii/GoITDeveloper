package goit.gojava7.ryzhkov.homework2.view;

import goit.gojava7.ryzhkov.homework2.model.Customer;

import java.util.Collection;

public interface CustomerView {

    int getId();

    String getName();

    Collection<Integer> getProjectsId();

    void write(Customer customer);

}
