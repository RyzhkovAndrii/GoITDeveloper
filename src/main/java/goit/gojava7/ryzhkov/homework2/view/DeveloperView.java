package goit.gojava7.ryzhkov.homework2.view;

import goit.gojava7.ryzhkov.homework2.model.Developer;

import java.util.Collection;

public interface DeveloperView {

    int getId();

    String getFirstName();

    String getLastName();

    double getSalary();

    Collection<Integer> getSkillsId();

    void write(Developer developer);

}
