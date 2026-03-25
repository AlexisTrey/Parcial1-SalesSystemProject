package co.edu.uptc.interfaces;

import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;

import java.time.LocalDate;
import java.util.List;

public interface PresenterInterface {
    void setModel(ModelInterface model);
    void setView(ViewInterface view);

    boolean addPerson(Person person);
    Person retireFromQueue();
    String[][] getPersonsAsTable();

    boolean addProduct(Product product);
    Product retireFromList(int id);
    String[][] getProductsAsTable();

    boolean addAccounting(Accounting accounting);
    List<Accounting> getAccountingMovements();

    void exportPersonsCsv();
    void exportProductsCsv();
    void exportAccountingCsv();
}
