package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.controller.implementation.CustomerControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.implementation.ProjectControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.interfaces.CustomerController;
import goit.gojava7.ryzhkov.homework2.model.Customer;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.View;

import java.sql.SQLException;
import java.util.Collection;

public class CustomerViewConsoleImpl implements View {

    private CustomerController customerController;

    public CustomerViewConsoleImpl() {
        customerController = new CustomerControllerImpl();
    }

    private void fillCustomerFields(Customer customer) throws SQLException {
        String name = ConsoleUtils.readString("Insert name of customer:");
        ConsoleUtils.writeString("Please insert customer's project's id  (input format: id1, id2, ...): ");
        Collection<Integer> projectId = ConsoleUtils.readIntCollection();
        Collection<Project> projects = new ProjectControllerImpl().getByCollectionId(projectId);
        customer.setName(name);
        customer.setProjects(projects);
    }

    @Override
    public void create() {
        try {
            Customer customer = new Customer();
            fillCustomerFields(customer);
            int id = customerController.save(customer);
            ConsoleUtils.writeString("OK! Customer was created. ID = " + id + ".");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getById() {
        int id = ConsoleUtils.readInt("Insert customer id:");
        try {
            ConsoleUtils.writeString(customerController.getById(id).toString());
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getAll() {
        try {
            customerController.getAll()
                    .stream()
                    .map(String::valueOf)
                    .forEach(ConsoleUtils::writeString);
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void update() {
        try {
            int id = ConsoleUtils.readInt("Insert customer id:");
            Customer customer = customerController.getById(id);
            fillCustomerFields(customer);
            customerController.update(customer);
            ConsoleUtils.writeString("OK! Customer was updated.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void removeById() {
        int id = ConsoleUtils.readInt("Insert customer id:");
        try {
            Customer customer = customerController.getById(id);
            customerController.remove(customer);
            ConsoleUtils.writeString("OK! Customer was deleted.");
        } catch (SQLException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

}
