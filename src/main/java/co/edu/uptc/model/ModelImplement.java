package co.edu.uptc.model;

import co.edu.uptc.model.util.Validator;
import co.edu.uptc.persistence.file.AccountingCsvHandler;
import co.edu.uptc.persistence.file.PersonCsvHandler;
import co.edu.uptc.persistence.file.ProductCsvHandler;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;
import co.edu.uptc.interfaces.ModelInterface;

import java.util.List;

public class ModelImplement implements ModelInterface {

    private final Validator validator = new Validator();
    private final PersonCsvHandler personHandler = new PersonCsvHandler();
    private final ProductCsvHandler productHandler = new ProductCsvHandler();
    private final AccountingCsvHandler accountingHandler = new AccountingCsvHandler();

    @Override
    public boolean validatePerson(Person p) { return validator.validate(p); }

    @Override
    public boolean validateProduct(Product p) { return validator.validate(p); }

    @Override
    public boolean validateAccounting(Accounting a) { return validator.validate(a); }

    @Override
    public List<Person> loadPersons() { return personHandler.load(); }

    @Override
    public void savePersons(List<Person> persons) { personHandler.save(persons); }

    @Override
    public List<Product> loadProducts() { return productHandler.load(); }

    @Override
    public void saveProducts(List<Product> products) { productHandler.save(products); }

    @Override
    public void appendAccounting(Accounting accounting) { accountingHandler.append(accounting); }

    @Override
    public List<Accounting> loadAccounting() { return accountingHandler.load(); }
}
