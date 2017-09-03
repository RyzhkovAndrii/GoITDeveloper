package goit.gojava7.ryzhkov.homework2.model;

import java.util.Collection;

public class Company {

    private int id;
    private String name;
    private Collection<Project> projects;

    public Company() {
    }

    public Company(String name, Collection<Project> projects) {
        this.name = name;
        this.projects = projects;
    }

    public Company(int id, String name, Collection<Project> projects) {
        this.id = id;
        this.name = name;
        this.projects = projects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return name + " (" + id + "), projects:" + projects;
    }
}
