package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.model.Company;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.CompanyView;

import java.util.Collection;

public class CompanyViewConsoleImpl implements CompanyView {

    @Override
    public int getId() {
        ConsoleUtils.writeString("Please insert companies ID: ");
        return ConsoleUtils.readInt();
    }

    @Override
    public String getName() {
        ConsoleUtils.writeString("Please insert companies name: ");
        return ConsoleUtils.readString();
    }

    @Override
    public Collection<Integer> getProjectsId() {
        ConsoleUtils.writeString("Please insert companies projects id (input format: id1, id2, ...): ");
        return ConsoleUtils.readIntCollection();
    }

    @Override
    public void write(Company company) {
        ConsoleUtils.writeString(company.toString());
    }

}
