package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.model.Skill;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.SkillView;

public class SkillViewConsoleImpl implements SkillView {

    @Override
    public int getId() {
        ConsoleUtils.writeString("Please insert skills ID: ");
        return ConsoleUtils.readInt();
    }

    @Override
    public String getName() {
        ConsoleUtils.writeString("Please insert skills name: ");
        return ConsoleUtils.readString();
    }

    @Override
    public void write(Skill skill) {
        ConsoleUtils.writeString(skill.toString());
    }

}
