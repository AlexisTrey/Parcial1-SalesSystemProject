package co.edu.uptc.view;

import co.edu.uptc.config.I18n;
import co.edu.uptc.util.Utilities;

import java.util.Scanner;

public abstract class BaseMenu {
    protected final Scanner scanner;
    protected final I18n i18n = I18n.getInstance();
    protected final ConsoleMenu menu = new ConsoleMenu();

    private final String exitLabel;

    protected BaseMenu(Scanner scanner) {
        this.scanner = scanner;
        this.exitLabel = i18n.get("menu.back");
    }

    protected BaseMenu(Scanner scanner, String exitLabel) {
        this.scanner = scanner;
        this.exitLabel = exitLabel;
    }

    public void show() {
        boolean running = true;
        while (running) {
            displayMenu();
            String option = readOption();
            running = handleOption(option);
        }
    }

    protected abstract String getTitle();

    protected abstract String[] getOptions();

    protected abstract boolean handleOption(String option);

    protected void displayMenu() {
        menu.showOptions(getTitle(), getOptions(), exitLabel);
    }

    protected String readOption() {
        System.out.print(Utilities.BLUE + i18n.get("menu.select") + ": " + Utilities.RESET);
        return scanner.nextLine().trim();
    }

    protected void showSuccess(String message) {
        System.out.println(Utilities.GREEN + message + Utilities.RESET);
    }

    protected void showError(String message) {
        System.out.println(Utilities.RED + message + Utilities.RESET);
    }

    protected void showInvalidOption() {
        showError(i18n.get("menu.invalid"));
    }
}
