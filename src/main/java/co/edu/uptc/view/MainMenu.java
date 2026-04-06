package co.edu.uptc.view;

import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.util.Utilities;

import java.util.Scanner;

public class MainMenu extends BaseMenu {
    private final ConsoleView view;
    private final PersonMenu personMenu;
    private final ProductMenu productMenu;
    private final AccountingMenu accountingMenu;

    public MainMenu(ConsoleView view, PresenterInterface presenter, Scanner scanner) {
        super(scanner);
        this.view = view;
        this.personMenu = new PersonMenu(view, presenter, scanner);
        this.productMenu = new ProductMenu(view, presenter, scanner);
        this.accountingMenu = new AccountingMenu(view, presenter, scanner);
    }

    @Override
    protected String getExitLabel() {
        return i18n.get("menu.exit");
    }

    @Override
    public void show() {
        printWelcome();
        super.show();
        printGoodbye();
    }

    @Override
    protected String getTitle() {
        return i18n.get("menu.main.title");
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                i18n.get("menu.main.persons"),
                i18n.get("menu.main.products"),
                i18n.get("menu.main.accounting")
        };
    }

    @Override
    protected boolean handleOption(String option) {
        switch (option) {
            case "1" -> personMenu.show();
            case "2" -> productMenu.show();
            case "3" -> accountingMenu.show();
            case "0" -> { return false; }
            default -> showInvalidOption();
        }
        return true;
    }

    private void printWelcome() {
        String text = i18n.get("menu.welcome");
        System.out.println(Utilities.GREEN);
        System.out.println("  +" + "=".repeat(40) + "+");
        System.out.printf("  |%s|%n", centerText(text, 40));
        System.out.println("  +" + "=".repeat(40) + "+");
        System.out.println(Utilities.RESET);
    }

    private void printGoodbye() {
        System.out.println(Utilities.GREEN + "\n  " + i18n.get("menu.goodbye") + "\n" + Utilities.RESET);
    }

    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text
                + " ".repeat(Math.max(0, width - text.length() - Math.max(0, padding)));
    }
}
