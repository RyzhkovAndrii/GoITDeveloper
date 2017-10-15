package goit.gojava7.ryzhkov.homework2.view.impl.console;

import goit.gojava7.ryzhkov.homework2.dao.StorageUtils;
import goit.gojava7.ryzhkov.homework2.view.View;
import goit.gojava7.ryzhkov.homework2.view.factory.ConsoleViewFactory;
import goit.gojava7.ryzhkov.homework2.view.factory.ViewFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ConsoleUtils {

    private static ViewFactory viewFactory = new ConsoleViewFactory();

    private static BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));

    private ConsoleUtils() {
    }

    public static void writeString(String string) {
        System.out.println(string);
    }

    public static String readString() {
        while (true) {
            try {
                return bufferedReader.readLine();
            } catch (IOException e) {
                writeString("Reading error. Please try again");
            }
        }
    }

    public static String readString(String request) {
        writeString(request);
        return readString();
    }

    public static int readInt(String request) {
        writeString(request);
        while (true) {
            try {
                return Integer.valueOf(readString());
            } catch (NumberFormatException e) {
                writeString("Incorrect input. Please try again");
            }
        }
    }

    public static double readDouble(String request) {
        writeString(request);
        while (true) {
            try {
                return Double.valueOf(readString());
            } catch (NumberFormatException e) {
                writeString("Incorrect input. Please try again");
            }
        }
    }

    public static Collection<Integer> readIntCollection() {
        while (true) {
            try {
                return Arrays.stream(ConsoleUtils.readString().split(","))
                        .map(String::trim)
                        .map(Integer::valueOf)
                        .collect(Collectors.toSet());
            } catch (NumberFormatException e) {
                writeString("Incorrect input. Please try again");
            }
        }
    }

    public static void closeReader() {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                writeString("Closing bufferedReader error.");
            }
        }
    }

    public static void showActionChooseMenu() {
        ConsoleUtils.writeString(
                "\n====== Choose action =====" +
                        "\n1 - get by id;" +
                        "\n2 - get all;" +
                        "\n3 - create;" +
                        "\n4 - update;" +
                        "\n5 - remove by id;" +
                        "\n6 - go to entity choose." +
                        "\nexit - to exit." +
                        "\nPlease, make your choice: ");
    }

    public static void showEntityChooseMenu() {
        ConsoleUtils.writeString(
                "\n====== Choose entity =====" +
                        "\n1 - skill;" +
                        "\n2 - developer;" +
                        "\n3 - project;" +
                        "\n4 - company;" +
                        "\n5 - customer;" +
                        "\nexit - to exit." +
                        "\nPlease, make your choice: ");

    }

    public static View entityMenuRequestProcessing(String request) {
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
                StorageUtils.closeAll();
                System.exit(0);
            default:
                ConsoleUtils.writeString("Incorrect input. Try again");
        }
        return view;
    }

    public static boolean actionMenuRequestProcessing(String request, View view) {
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
                StorageUtils.closeAll();
                System.exit(0);
            default:
                ConsoleUtils.writeString("Incorrect input. Try again");
                return true;
        }
    }

}
