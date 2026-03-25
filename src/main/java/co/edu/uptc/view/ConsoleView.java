package co.edu.uptc.view;

import java.time.LocalDate;
import java.util.Scanner;

import co.edu.uptc.config.I18n;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;
import co.edu.uptc.util.DateFormatter;
import co.edu.uptc.util.Utilities;

public class ConsoleView implements ViewInterface {
    private PresenterInterface presenter;
    private final Scanner scanner = new Scanner(System.in);
    private final I18n i18n = I18n.getInstance();
    private MainMenu mainMenu;

    @Override
    public void setPresenter(PresenterInterface presenter) {
        this.presenter = presenter;
        this.mainMenu = new MainMenu(this, presenter, scanner);
    }

    @Override
    public void start() {
        mainMenu.show();
    }

    public String readLine(String prompt) {
        System.out.print(Utilities.BLUE + "  " + prompt + Utilities.RESET);
        return scanner.nextLine().trim();
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            String input = readLine(prompt);
            LocalDate date = DateFormatter.parse(input);
            if (date != null && !date.isAfter(LocalDate.now())) {
                return date;
            }
            showError(i18n.get("error.invalid.date"));
        }
    }

    public double readPositiveDouble(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                double value = Double.parseDouble(input.replace(",", "."));
                if (value > 0) return value;
                showError(i18n.get("error.positive.value"));
            } catch (NumberFormatException e) {
                showError(i18n.get("error.invalid.number"));
            }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value > 0) return value;
                showError(i18n.get("error.positive.value"));
            } catch (NumberFormatException e) {
                showError(i18n.get("error.invalid.int"));
            }
        }
    }

    public String selectFromOptions(String prompt, String[] options) {
        System.out.println(Utilities.BLUE + "  " + prompt + Utilities.RESET);
        for (int i = 0; i < options.length; i++) {
            System.out.printf("    %2d. %s%n", i + 1, options[i]);
        }
        while (true) {
            String input = readLine(i18n.get("field.select") + ": ");
            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= options.length) {
                    return options[index - 1];
                }
            } catch (NumberFormatException ignored) {
            }
            showError(i18n.get("menu.invalid"));
        }
    }

    public void showMessage(String message) {
        System.out.println(Utilities.GREEN + "  " + message + Utilities.RESET);
    }

    public void showError(String message) {
        System.out.println(Utilities.RED + "  " + message + Utilities.RESET);
    }
}
