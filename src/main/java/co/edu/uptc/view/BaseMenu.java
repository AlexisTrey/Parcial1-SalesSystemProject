package co.edu.uptc.view;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.config.I18n;
import co.edu.uptc.util.TablePrinter;
import co.edu.uptc.util.Utilities;

import java.util.Arrays;
import java.util.Scanner;

public abstract class BaseMenu {
    protected final I18n i18n = I18n.getInstance();
    protected final Scanner scanner;
    private final int pageSize = AppConfig.getInstance().getInt("ui.page.size", 10);
    private static final int MENU_WIDTH = 42;

    protected BaseMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean running = true;
        while (running) {
            printMenuHeader();
            String option = readMenuOption();
            System.out.println();
            running = handleOption(option);
        }
    }

    private void printMenuHeader() {
        System.out.println();
        System.out.println(Utilities.CYAN + "  " + "-".repeat(MENU_WIDTH) + Utilities.RESET);
        System.out.println(Utilities.CYAN + "  " + centerText(getTitle(), MENU_WIDTH) + Utilities.RESET);
        System.out.println(Utilities.CYAN + "  " + "-".repeat(MENU_WIDTH) + Utilities.RESET);
        String[] options = getOptions();
        for (int i = 0; i < options.length; i++) {
            System.out.printf("  %3d. %s%n", i + 1, options[i]);
        }
        System.out.printf("  %3d. %s%n", 0, getExitLabel());
        System.out.println(Utilities.CYAN + "  " + "-".repeat(MENU_WIDTH) + Utilities.RESET);
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) return text;
        int pad = (width - text.length()) / 2;
        return " ".repeat(pad) + text;
    }

    private String readMenuOption() {
        System.out.print(Utilities.BLUE + "  " + i18n.get("menu.select") + ": " + Utilities.RESET);
        return scanner.nextLine().trim();
    }

    protected void printPaginated(String[] headers, String[][] rows) {
        printPaginated(headers, rows, false);
    }

    protected void printPaginated(String[] headers, String[][] rows, boolean allLeftAlign) {
        if (rows.length == 0) {
            showInfo(i18n.get("list.empty"));
            return;
        }
        int total = rows.length;
        int pages = (int) Math.ceil((double) total / pageSize);
        int page = 0;

        while (page < pages) {
            int from = page * pageSize;
            int to = Math.min(from + pageSize, total);
            String[][] pageRows = Arrays.copyOfRange(rows, from, to);
            TablePrinter.print(headers, pageRows, allLeftAlign);
            if (pages > 1) {
                System.out.printf(Utilities.YELLOW + "  %s %d/%d — %s%n" + Utilities.RESET,
                        i18n.get("page.label"), page + 1, pages, i18n.get("page.prompt"));
                String input = scanner.nextLine().trim();
                if ("0".equals(input)) break;
            }
            page++;
        }
    }

    protected void showSuccess(String message) {
        System.out.println(Utilities.GREEN + "  " + message + Utilities.RESET);
    }

    protected void showError(String message) {
        System.out.println(Utilities.RED + "  [!] " + message + Utilities.RESET);
    }

    protected void showInfo(String message) {
        System.out.println(Utilities.YELLOW + "  " + message + Utilities.RESET);
    }

    protected void showInvalidOption() {
        showError(i18n.get("menu.invalid"));
    }

    protected String getExitLabel() {
        return i18n.get("menu.back");
    }

    protected abstract String getTitle();
    protected abstract String[] getOptions();
    protected abstract boolean handleOption(String option);
}
