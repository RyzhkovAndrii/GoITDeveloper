package goit.gojava7.ryzhkov.homework2.model;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "developers")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "developer_id")
    private int id;

    @Column(name = "developer_first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "developer_last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "developer_salary")
    private double salary;

    @ManyToMany(cascade = {PERSIST, MERGE, REFRESH, DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "developers_skills",
            joinColumns = @JoinColumn(name = "developer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "skill_id", nullable = false))
    private Collection<Skill> skills;

    public Developer() {
    }

    public Developer(String firstName, String lastName, double salary, Collection<Skill> skills) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.skills = skills;
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
        return firstName + " " + lastName + " (" + id + "), " + salary + "$, skills: " + skills;
    }
}
