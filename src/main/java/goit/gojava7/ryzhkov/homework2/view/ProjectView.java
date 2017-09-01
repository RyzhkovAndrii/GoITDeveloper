package goit.gojava7.ryzhkov.homework2.view;

import goit.gojava7.ryzhkov.homework2.model.Project;

import java.util.Collection;

public interface ProjectView {

    int getId();

    String getName();

    Collection<Integer> getDevelopersId();

    void write(Project project);

}
