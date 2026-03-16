package co.edu.uptc.interfaces;

import java.time.LocalDate;

public interface PresenterInterface {
    void setModel(ModelInterface model);

    void setView(ViewInterface view);

    boolean addPerson(String name, String lastName, String gender, LocalDate birthdate);

    String[][] getPersonsAsTable();

    boolean addProduct(String description, String unit, double price);

    String[][] getProductsAsTable();

    boolean addAccounting(String description, String type, double amount);

    String[][] getAccountingAsTable();

    double getAccountingTotal();
}
