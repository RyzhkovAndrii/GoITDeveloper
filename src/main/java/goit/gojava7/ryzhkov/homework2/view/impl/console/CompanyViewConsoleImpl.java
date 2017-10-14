package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.controller.implementation.CompanyControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.implementation.ProjectControllerImpl;
import goit.gojava7.ryzhkov.homework2.controller.interfaces.CompanyController;
import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.model.Company;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.view.View;

import java.util.Collection;

public class CompanyViewConsoleImpl implements View {

    private CompanyController companyController;

    public CompanyViewConsoleImpl() {
        companyController = new CompanyControllerImpl();
    }

    private void fillCompanyFields(Company company) throws StorageException {
        String name = ConsoleUtils.readString("Insert name of company:");
        ConsoleUtils.writeString("Please insert company's project's id  (input format: id1, id2, ...): ");
        Collection<Integer> projectId = ConsoleUtils.readIntCollection();
        Collection<Project> projects = new ProjectControllerImpl().getByIdRange(projectId);
        company.setName(name);
        company.setProjects(projects);
    }

    @Override
    public void create() {
        try {
            Company company = new Company();
            fillCompanyFields(company);
            int id = companyController.save(company);
            ConsoleUtils.writeString("OK! Company was created. ID = " + id + ".");
        } catch (StorageException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getById() {
        int id = ConsoleUtils.readInt("Insert company id:");
        try {
            ConsoleUtils.writeString(companyController.getById(id).toString());
        } catch (StorageException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void getAll() {
        try {
            companyController.getAll()
                    .stream()
                    .map(String::valueOf)
                    .forEach(ConsoleUtils::writeString);
        } catch (StorageException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void update() {
        try {
            int id = ConsoleUtils.readInt("Insert company id:");
            Company company = companyController.getById(id);
            fillCompanyFields(company);
            companyController.update(company);
            ConsoleUtils.writeString("OK! Company was updated.");
        } catch (StorageException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

    @Override
    public void removeById() {
        int id = ConsoleUtils.readInt("Insert company id:");
        try {
            Company company = companyController.getById(id);
            companyController.remove(company);
            ConsoleUtils.writeString("OK! Company was deleted.");
        } catch (StorageException e) {
            ConsoleUtils.writeString(e.getMessage());
        }
    }

}
