package goit.gojava7.ryzhkov.homework2.view.factory;

import goit.gojava7.ryzhkov.homework2.view.View;

public interface ViewFactory {

    View getSkillView();

    View getDeveloperView();

    View getProjectView();

    View getCompanyView();

    View getCustomerView();

}
