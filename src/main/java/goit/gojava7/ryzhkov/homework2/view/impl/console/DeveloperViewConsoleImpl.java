package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.controller.implementation.DeveloperControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.implementation.SkillControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.interfaces.DeveloperController;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Skill;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.View;

import java.sql.SQLException;
import java.util.Collection;

public class DeveloperViewConsoleImpl implements View {

    private DeveloperController developerController;

    public DeveloperViewConsoleImpl() {
        developerController = new DeveloperControllerImpl();
    }

    private void fillDevelopersFields(Developer developer) throws SQLException {
        String firstName = ConsoleUtils.readString("Insert developer's first name:");
        String lastName = ConsoleUtils.readString("Insert developer's last name:");
        double salary = ConsoleUtils.readDouble("Insert developer's salary");
        ConsoleUtils.writeString("Please insert developer's skills id (input format: id1, id2, ...): ");
        Collection<Integer> skillsId = ConsoleUtils.readIntCollection();
        Collection<Skill> skills = new SkillControllerImpl().getByCollectionId(skillsId);
        developer.setFirstName(firstName);
        developer.setLastName(lastName);
        developer.setSalary(salary);
        developer.setSkills(skills);
    }

    @Override
    public void create() {
        try {
            Developer developer = new Developer();
            fillDevelopersFields(developer);
            int id = developerController.save(developer);
            ConsoleUtils.writeString("OK! Developer was created. ID = " + id + ".");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getById() {
        int id = ConsoleUtils.readInt("Insert developer id:");
        try {
            ConsoleUtils.writeString(developerController.getById(id).toString());
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getAll() {
        try {
            developerController.getAll()
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
            int id = ConsoleUtils.readInt("Insert developer id:");
            Developer developer = developerController.getById(id);
            fillDevelopersFields(developer);
            developerController.update(developer);
            ConsoleUtils.writeString("OK! Developer was updated.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void removeById() {
        int id = ConsoleUtils.readInt("Insert developer id:");
        try {
            Developer developer = developerController.getById(id);
            developerController.remove(developer);
            ConsoleUtils.writeString("OK! Developer was deleted.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

}
