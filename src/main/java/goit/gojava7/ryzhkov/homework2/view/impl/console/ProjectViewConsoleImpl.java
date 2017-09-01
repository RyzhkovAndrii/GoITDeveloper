package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.ProjectView;

import java.util.Collection;

public class ProjectViewConsoleImpl implements ProjectView {

    @Override
    public int getId() {
        ConsoleUtils.writeString("Please insert projects ID: ");
        return ConsoleUtils.readInt();
    }

    @Override
    public String getName() {
        ConsoleUtils.writeString("Please insert projects name: ");
        return ConsoleUtils.readString();
    }

    @Override
    public Collection<Integer> getDevelopersId() {
        ConsoleUtils.writeString("Please insert projects' developer's id (input format: id1, id2, ...): ");
        return ConsoleUtils.readIntCollection();
    }

    @Override
    public void write(Project project) {
        ConsoleUtils.writeString(project.toString());
    }

}
