package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.DeveloperView;

import java.util.Collection;

public class DeveloperViewConsoleImpl implements DeveloperView {

    @Override
    public int getId() {
        ConsoleUtils.writeString("Please insert developers ID: ");
        return ConsoleUtils.readInt();
    }

    @Override
    public String getFirstName() {
        ConsoleUtils.writeString("Please insert developers first name: ");
        return ConsoleUtils.readString();
    }

    @Override
    public String getLastName() {
        ConsoleUtils.writeString("Please insert developers last name: ");
        return ConsoleUtils.readString();
    }

    @Override
    public double getSalary() {
        ConsoleUtils.writeString("Please insert developers salary: ");
        return ConsoleUtils.readDouble();
    }

    @Override
    public Collection<Integer> getSkillsId() {
        ConsoleUtils.writeString("Please insert developers skills id (input format: id1, id2, ...): ");
        return ConsoleUtils.readIntCollection();
    }

    @Override
    public void write(Developer developer) {
        ConsoleUtils.writeString(developer.toString());
    }

}
