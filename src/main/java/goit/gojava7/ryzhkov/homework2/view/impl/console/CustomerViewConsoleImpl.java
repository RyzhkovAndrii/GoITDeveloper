package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.model.Customer;
import goit.gojava7.ryzhkov.homework2.utils.ConsoleUtils;
import goit.gojava7.ryzhkov.homework2.view.CustomerView;

import java.util.Collection;

public class CustomerViewConsoleImpl implements CustomerView {

    @Override
    public int getId() {
        ConsoleUtils.writeString("Please insert customers ID: ");
        return ConsoleUtils.readInt();
    }

    @Override
    public String getName() {
        ConsoleUtils.writeString("Please insert customers name: ");
        return ConsoleUtils.readString();
    }

    @Override
    public Collection<Integer> getProjectsId() {
        ConsoleUtils.writeString("Please insert customers projects id (input format: id1, id2, ...): ");
        return ConsoleUtils.readIntCollection();
    }

    @Override
    public void write(Customer customer) {
        ConsoleUtils.writeString(customer.toString());
    }

}
