package goit.gojava7.ryzhkov.homework2.view.factory;

import goit.gojava7.ryzhkov.homework2.view.*;

public interface ViewFactory {

    SkillView getSkillView();

    DeveloperView getDeveloperView();

    ProjectView getProjectView();

    CompanyView getCompanyView();

    CustomerView getCustomerView();

}
