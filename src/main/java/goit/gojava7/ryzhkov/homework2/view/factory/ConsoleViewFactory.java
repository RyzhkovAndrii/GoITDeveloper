package goit.gojava7.ryzhkov.homework2.view.factory;

import goit.gojava7.ryzhkov.homework2.view.*;
import goit.gojava7.ryzhkov.homework2.view.impl.console.*;

public class ConsoleViewFactory implements ViewFactory {

    @Override
    public SkillView getSkillView() {
        return new SkillViewConsoleImpl();
    }

    @Override
    public DeveloperView getDeveloperView() {
        return new DeveloperViewConsoleImpl();
    }

    @Override
    public ProjectView getProjectView() {
        return new ProjectViewConsoleImpl();
    }

    @Override
    public CompanyView getCompanyView() {
        return new CompanyViewConsoleImpl();
    }

    @Override
    public CustomerView getCustomerView() {
        return new CustomerViewConsoleImpl();
    }

}
