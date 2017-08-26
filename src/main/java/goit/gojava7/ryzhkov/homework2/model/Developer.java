package goit.gojava7.ryzhkov.homework2.model;

import java.util.Collection;

public class Developer {

    private int id;
    private String firstName;
    private String lastName;
    private double salary;
    private Collection<Skill> skills;

    public Developer() {
    }

    public Developer(int id, String firstName, String lastName, double salary, Collection<Skill> skills) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Collection<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Collection<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Developer{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
                + '\'' + ", skills=" + skills + '}';
    }
}
