package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.controller.implementation.DeveloperControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.implementation.ProjectControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.interfaces.ProjectController;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.view.View;

import java.sql.SQLException;
import java.util.Collection;

public class ProjectViewConsoleImpl implements View {

    private ProjectController projectController;

    public ProjectViewConsoleImpl() {
        projectController = new ProjectControllerImpl();
    }

    private void fillProjectsFields(Project project) throws SQLException {
        String name = ConsoleUtils.readString("Insert name of project:");
        double cost = ConsoleUtils.readDouble("Insert cost of project:");
        ConsoleUtils.writeString("Please insert project's developers id  (input format: id1, id2, ...): ");
        Collection<Integer> developerId = ConsoleUtils.readIntCollection();
        Collection<Developer> developers = new DeveloperControllerImpl().getByCollectionId(developerId);
        project.setName(name);
        project.setCost(cost);
        project.setDevelopers(developers);
    }

    @Override
    public void create() {
        try {
            Project project = new Project();
            fillProjectsFields(project);
            int id = projectController.save(project);
            ConsoleUtils.writeString("OK! Project was created. ID = " + id + ".");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getById() {
        int id = ConsoleUtils.readInt("Insert project id:");
        try {
            ConsoleUtils.writeString(projectController.getById(id).toString());
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getAll() {
        try {
            projectController.getAll()
                    .stream()
                    .map(String::valueOf)
                    .forEach(ConsoleUtils::writeString);
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void update() {
        try {
            int id = ConsoleUtils.readInt("Insert project id:");
            Project project = projectController.getById(id);
            fillProjectsFields(project);
            projectController.update(project);
            ConsoleUtils.writeString("OK! Project was updated.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void removeById() {
        int id = ConsoleUtils.readInt("Insert project id:");
        try {
            Project project = projectController.getById(id);
            projectController.remove(project);
            ConsoleUtils.writeString("OK! Project was deleted.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

}
