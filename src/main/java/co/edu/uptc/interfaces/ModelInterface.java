package co.edu.uptc.interfaces;

import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;

import java.util.List;

public interface ModelInterface {
    boolean validatePerson(Person person);
    boolean validateProduct(Product product);
    boolean validateAccounting(Accounting accounting);

    List<Person> loadPersons();
    void savePersons(List<Person> persons);

    List<Product> loadProducts();
    void saveProducts(List<Product> products);

    void appendAccounting(Accounting accounting);
    List<Accounting> loadAccounting();
}
