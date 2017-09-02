package goit.gojava7.ryzhkov.homework2.view.factory;

import goit.gojava7.ryzhkov.homework2.view.View;
import goit.gojava7.ryzhkov.homework2.view.impl.console.*;

public class ConsoleViewFactory implements ViewFactory {

    @Override
    public View getSkillView() {
        return new SkillViewConsoleImpl();
    }

    @Override
    public View getDeveloperView() {
        return new DeveloperViewConsoleImpl();
    }

    @Override
    public View getProjectView() {
        return new ProjectViewConsoleImpl();
    }

    @Override
    public View getCompanyView() {
        return new CompanyViewConsoleImpl();
    }

    @Override
    public View getCustomerView() {
        return new CustomerViewConsoleImpl();
    }

}
