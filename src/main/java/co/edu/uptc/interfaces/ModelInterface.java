package co.edu.uptc.interfaces;

import co.edu.uptc.entity.Accounting;
import co.edu.uptc.entity.Person;
import co.edu.uptc.entity.Product;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ModelInterface {
    boolean addPerson(String name, String lastName, String gender, LocalDate birthdate);

    List<Person> getPersons();

    boolean addProduct(String description, String unit, double price);

    List<Product> getProducts();

    boolean addAccounting(String description, String type, double amount);

    List<Accounting> getAccountingMovements();

    double getAccountingTotal();
}
