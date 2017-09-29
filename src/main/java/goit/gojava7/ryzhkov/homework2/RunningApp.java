package goit.gojava7.ryzhkov.homework2;

import goit.gojava7.ryzhkov.homework2.utils.ConnectionUtils;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.View;
import goit.gojava7.ryzhkov.homework2.view.factory.ConsoleViewFactory;
import goit.gojava7.ryzhkov.homework2.view.factory.ViewFactory;

import java.sql.SQLException;

public class RunningApp {

    private static ViewFactory viewFactory = new ConsoleViewFactory();

    private static View entityMenuRequestProcessing(String request) {
        View view = null;
        switch (request) {
            case "1":
                view = viewFactory.getSkillView();
                break;
            case "2":
                view = viewFactory.getDeveloperView();
                break;
            case "3":
                view = viewFactory.getProjectView();
                break;
            case "4":
                view = viewFactory.getCompanyView();
                break;
            case "5":
                view = viewFactory.getCustomerView();
                break;
            case "exit":
                ConsoleUtils.closeReader();
                ConnectionUtils.closeConnection();
                System.exit(0);
            default:
                ConsoleUtils.writeString("Incorrect input. Try again");
        }
        return view;
    }

    private static boolean actionMenuRequestProcessing(String request, View view) {
        switch (request) {
            case "1":
                view.getById();
                return true;
            case "2":
                view.getAll();
                return true;
            case "3":
                view.create();
                return true;
            case "4":
                view.update();
                return true;
            case "5":
                view.removeById();
                return true;
            case "6":
                return false;
            case "exit":
                ConsoleUtils.closeReader();
                ConnectionUtils.closeConnection();
                System.exit(0);
            default:
                ConsoleUtils.writeString("Incorrect input. Try again");
                return true;
        }
    }

    public static void main(String[] args) throws SQLException {

        while (true) {
            ConsoleUtils.showEntityChooseMenu();
            String entityMenuRequest = ConsoleUtils.readString();
            View view = entityMenuRequestProcessing(entityMenuRequest);
            if (view != null) {
                while (true) {
                    ConsoleUtils.showActionChooseMenu();
                    String actionMenuRequest = ConsoleUtils.readString();
                    if (!actionMenuRequestProcessing(actionMenuRequest, view)) {
                        break;
                    }
                }
            }

        }
    }

}
