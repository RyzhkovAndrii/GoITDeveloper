package goit.gojava7.ryzhkov.homework2.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;


@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private int id;

    @Column(name = "company_name", length = 50, nullable = false)
    private String name;

    @ManyToMany(cascade = {PERSIST, MERGE, REFRESH, DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "companies_projects",
            joinColumns = @JoinColumn(name = "company_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "project_id", nullable = false))
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
        return name + " (" + id + "), projects:" +
                projects.stream()
                        .map(Project::getName)
                        .collect(Collectors.toList());
    }
}
