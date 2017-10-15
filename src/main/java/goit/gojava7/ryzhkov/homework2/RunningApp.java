package goit.gojava7.ryzhkov.homework2;

import goit.gojava7.ryzhkov.homework2.dao.factories.dao.DaoFactoryFactory;
import goit.gojava7.ryzhkov.homework2.dao.factories.dao.HibernateDaoFactory;
import goit.gojava7.ryzhkov.homework2.view.View;
import goit.gojava7.ryzhkov.homework2.view.impl.console.ConsoleUtils;

public class RunningApp {

    public static void main(String[] args) {

        DaoFactoryFactory.setDaoFactory(new HibernateDaoFactory());

        while (true) {
            ConsoleUtils.showEntityChooseMenu();
            String entityMenuRequest = ConsoleUtils.readString();
            View view = ConsoleUtils.entityMenuRequestProcessing(entityMenuRequest);
            while (true) {
                ConsoleUtils.showActionChooseMenu();
                String actionMenuRequest = ConsoleUtils.readString();
                if (!ConsoleUtils.actionMenuRequestProcessing(actionMenuRequest, view)) {
                    break;
                }
            }
        }
    }

}
