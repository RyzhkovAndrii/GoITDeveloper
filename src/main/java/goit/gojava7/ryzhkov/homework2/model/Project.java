package goit.gojava7.ryzhkov.homework2.model;

import java.util.Collection;

public class Project {

    private int id;
    private String name;
    private double cost;
    private Collection<Developer> developers;

    public Project() {
    }

    public Project(String name, double cost, Collection<Developer> developers) {
        this.name = name;
        this.cost = cost;
        this.developers = developers;
    }

    public Project(int id, String name, double cost, Collection<Developer> developers) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.developers = developers;
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

    public Collection<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Collection<Developer> developers) {
        this.developers = developers;
    }

    @Override
    public String toString() {
        return name + "(" + id + "), dev: " + developers;
    }
}
