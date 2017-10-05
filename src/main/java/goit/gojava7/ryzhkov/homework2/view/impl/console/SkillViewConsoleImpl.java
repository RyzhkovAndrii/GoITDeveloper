package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.controller.implementation.SkillControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.interfaces.SkillController;
import goit.gojava7.ryzhkov.homework2.model.Skill;
import goit.gojava7.ryzhkov.homework2.view.View;

import java.sql.SQLException;

public class SkillViewConsoleImpl implements View {

    private SkillController skillController;

    public SkillViewConsoleImpl() {
        skillController = new SkillControllerImpl();
    }

    @Override
    public void create() {
        String name = ConsoleUtils.readString("Insert name of skill:");
        Skill skill = new Skill(name);
        try {
            int id = skillController.save(skill);
            ConsoleUtils.writeString("OK! Skill was created. ID = " + id + ".");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getById() {
        int id = ConsoleUtils.readInt("Insert skill id:");
        try {
            ConsoleUtils.writeString(skillController.getById(id).toString());
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getAll() {
        try {
            skillController.getAll()
                    .stream()
                    .map(String::valueOf)
                    .forEach(ConsoleUtils::writeString);
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void update() {
        int id = ConsoleUtils.readInt("Insert skill id:");
        String name = ConsoleUtils.readString("Insert name of skill:");
        try {
            Skill skill = skillController.getById(id);
            skill.setName(name);
            skillController.update(skill);
            ConsoleUtils.writeString("OK! Skill was updated.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void removeById() {
        int id = ConsoleUtils.readInt("Insert skill id:");
        try {
            Skill skill = skillController.getById(id);
            skillController.remove(skill);
            ConsoleUtils.writeString("OK! Skill was deleted.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

}
