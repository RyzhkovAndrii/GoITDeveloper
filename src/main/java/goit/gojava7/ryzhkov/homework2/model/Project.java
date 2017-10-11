package goit.gojava7.ryzhkov.homework2.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int id;

    @Column(name = "project_name", length = 50, nullable = false)
    private String name;

    @Column(name = "project_cost")
    private double cost;

    @ManyToMany(cascade = {PERSIST, MERGE, REFRESH, DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "projects_developers",
            joinColumns = @JoinColumn(name = "project_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "developer_id", nullable = false))
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Collection<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Collection<Developer> developers) {
        this.developers = developers;
    }

    @Override
    public String toString() {
        return name + "(" + id + "), " + cost + "$, dev: " +
                developers.stream()
                        .map((Developer::getLastName))
                        .collect(Collectors.toList());
    }
}
