package co.edu.uptc.view;

import java.util.Scanner;

import co.edu.uptc.util.Utilities;

public class OptionSelector {
    private final Scanner scanner;

    public OptionSelector(Scanner scanner) {
        this.scanner = scanner;
    }

    public String selectOption(String title, String[] options) {

        showOptions(title, options);
        return getOption(options);
    }

    private void showOptions(String title, String[] options) {

        System.out.println(Utilities.AZUL);
        System.out.println("\n--- " + title + " ---");

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.print("Seleccione número: ");
        System.out.print(Utilities.RESET);
    }

    private String getOption(String[] options) {

        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            if (index >= 0 && index < options.length) {
                return options[index];
            }

        } catch (NumberFormatException ignored) {
        }

        System.out.println(Utilities.ROJO + "Opcion invalida" + Utilities.RESET);
        return options[0];
    }
}
