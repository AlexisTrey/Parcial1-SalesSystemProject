package co.edu.uptc.interfaces;

import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;

import java.time.LocalDate;
import java.util.List;

public interface ModelInterface {
    boolean addPerson(Person person);
    Person retireFromQueue();
    List<Person> getPersons();

    boolean addProduct(Product product);
    Product retireFromList(int id);
    List<Product> getProducts();

    boolean addAccounting(Accounting accounting);
    List<Accounting> getAccountingMovements();
    double getIncomeTotal();
    double getExpenseTotal();
    double getNetTotal();

    void exportPersonsCsv();
    void exportProductsCsv();
    void exportAccountingCsv();
}
