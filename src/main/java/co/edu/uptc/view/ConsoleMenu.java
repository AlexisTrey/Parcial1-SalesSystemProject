package co.edu.uptc.view;

import java.util.Scanner;

import co.edu.uptc.util.Utilities;

public class ConsoleMenu {
    private final Scanner scanner;

    public ConsoleMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public String showMenu(String title, String[] options) {

        System.out.println(Utilities.AZUL);
        System.out.println("====================================");
        System.out.println("   " + title);
        System.out.println("====================================");

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.println("0. Salir");
        System.out.println("====================================");
        System.out.print("Seleccione una opcion: ");
        System.out.print(Utilities.RESET);

        return scanner.nextLine();
    }
}
