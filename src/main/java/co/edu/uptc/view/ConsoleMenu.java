package co.edu.uptc.view;

import co.edu.uptc.config.I18n;
import co.edu.uptc.util.Utilities;

public class ConsoleMenu {
    private final I18n i18n = I18n.getInstance();

    public void showOptions(String title, String[] options, String exitLabel) {
        printHeader(title);
        printOptions(options);
        printExitOption(exitLabel);
        printFooter();
    }

    private void printHeader(String title) {
        System.out.println(Utilities.GREEN);
        System.out.println("+========================================+");
        System.out.println("   " + centerText(title, 36) + "   ");
        System.out.println("+========================================+");
    }

    private void printOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            printOption(i + 1, options[i]);
        }
    }

    private void printOption(int number, String text) {
        System.out.printf("|  %d. %-34s |%n", number, text);
    }

    private void printExitOption(String exitLabel) {
        System.out.printf("|  0. %-34s |%n", exitLabel);
    }

    private void printFooter() {
        System.out.println("+========================================+");
        System.out.print(Utilities.RESET);
    }

    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
}
