package co.edu.uptc.interfaces;

import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;

import java.util.List;

public interface PresenterInterface {
    void setModel(ModelInterface model);
    void setView(ViewInterface view);

    boolean addPerson(Person person);
    Person retireFromQueue();
    String[][] getPersonsAsTable();

    boolean addProduct(Product product);
    Product retireFromStack();
    String[][] getProductsAsTable();

    boolean addAccounting(Accounting accounting);
    List<Accounting> getAccountingMovements();

    void exportPersonsCsv();
    void exportProductsCsv();
}
