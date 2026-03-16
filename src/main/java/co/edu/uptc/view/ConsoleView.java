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

    @Override
    public void setPresenter(PresenterInterface presenter) {
        this.presenter = presenter;
    }

    @Override
    public void start() {
        new MainMenu(this, presenter, scanner).show();
    }

    public String readLine(String prompt) {
        System.out.print(Utilities.BLUE + prompt + Utilities.RESET);
        return scanner.nextLine().trim();
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            String input = readLine(prompt);
            LocalDate date = DateFormatter.parse(input);
            if (date != null)
                return date;
            showError(i18n.get("error.invalid.date"));
        }
    }

    public double readPositiveDouble(String prompt) {
        while (true) {
            try {
                double value = Double.parseDouble(readLine(prompt));
                if (value > 0)
                    return value;
                showError(i18n.get("error.positive.value"));
            } catch (NumberFormatException e) {
                showError(i18n.get("error.invalid.number"));
            }
        }
    }

    public String selectFromOptions(String prompt, String[] options) {
        showOptions(prompt, options);
        return readSelectedOption(options);
    }

    public void showMessage(String message) {
        System.out.println(Utilities.GREEN + message + Utilities.RESET);
    }

    public void showError(String message) {
        System.out.println(Utilities.RED + message + Utilities.RESET);
    }

    private void showOptions(String prompt, String[] options) {
        System.out.println(Utilities.BLUE + prompt + Utilities.RESET);
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ". " + options[i]);
        }
    }

    private String readSelectedOption(String[] options) {
        while (true) {
            String input = readLine(i18n.get("field.select") + ": ");
            int index = parseIndex(input);
            if (isValidIndex(index, options.length)) {
                return options[index - 1];
            }
            showError(i18n.get("menu.invalid"));
        }
    }

    private int parseIndex(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isValidIndex(int index, int size) {
        return index >= 1 && index <= size;
    }
}
