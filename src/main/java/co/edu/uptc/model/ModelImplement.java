package co.edu.uptc.model;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.model.util.DataDestination;
import co.edu.uptc.model.util.Validator;
import co.edu.uptc.persistence.file.AccountingCsvHandler;
import co.edu.uptc.persistence.file.PersonCsvHandler;
import co.edu.uptc.persistence.file.ProductCsvHandler;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;
import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.model.util.DoubleList;

import java.time.LocalDateTime;
import java.util.List;

public class ModelImplement implements ModelInterface {

    private final DoubleList<Person> persons = new DoubleList<>();
    private final DoubleList<Product> products = new DoubleList<>();
    private final DoubleList<Accounting> accountingMovements = new DoubleList<>();

    private final PersonCsvHandler personCsvHandler = new PersonCsvHandler();
    private final ProductCsvHandler productCsvHandler = new ProductCsvHandler();
    private final AccountingCsvHandler accountingCsvHandler = new AccountingCsvHandler();

    private final AppConfig config = AppConfig.getInstance();

    private int personIdCounter = 1;
    private int productIdCounter = 1;

    public ModelImplement() {
        loadAll();
    }

    private void loadAll() {
        loadPersons();
        loadProducts();
        loadAccounting();
    }

    private void loadPersons() {
        for (Person p : personCsvHandler.load()) {
            store(p, DataDestination.QUEUE);
            if (p.getId() >= personIdCounter) personIdCounter = p.getId() + 1;
        }
    }

    private void loadProducts() {
        for (Product p : productCsvHandler.load()) {
            store(p, DataDestination.LIST);
            if (p.getId() >= productIdCounter) productIdCounter = p.getId() + 1;
        }
    }

    private void loadAccounting() {
        List<Accounting> loaded = accountingCsvHandler.load();
        for (int i = loaded.size() - 1; i >= 0; i--) {
            store(loaded.get(i), DataDestination.STACK);
        }
    }

    @SuppressWarnings("unchecked")
    private void store(Object data, DataDestination destination) {
        switch (destination) {
            case QUEUE -> persons.addLast((Person) data);
            case STACK -> accountingMovements.addFirst((Accounting) data);
            case LIST  -> products.addLast((Product) data);
        }
    }

    @Override
    public boolean addPerson(Person person) {
        if (!Validator.isValidPerson(person, config)) return false;
        person.setId(personIdCounter++);
        person.setName(person.getName().trim());
        person.setLastName(person.getLastName().trim());
        store(person, DataDestination.QUEUE);
        exportPersonsCsv();
        return true;
    }

    @Override
    public Person retireFromQueue() {
        Person retired = persons.removeFirst();
        if (retired != null) exportPersonsCsv();
        return retired;
    }

    @Override
    public List<Person> getPersons() {
        return persons.getAllList();
    }

    @Override
    public boolean addProduct(Product product) {
        if (!Validator.isValidProduct(product, config)) return false;
        product.setId(productIdCounter++);
        product.setDescription(normalizeDescription(product.getDescription()));
        store(product, DataDestination.LIST);
        exportProductsCsv();
        return true;
    }

    @Override
    public Product retireFromList(int id) {
        Product found = products.findWhere(p -> p.getId() == id);
        if (found == null) return null;
        products.removeWhere(p -> p.getId() == id);
        exportProductsCsv();
        return found;
    }

    @Override
    public List<Product> getProducts() {
        return products.getAllList();
    }

    @Override
    public boolean addAccounting(Accounting accounting) {
        if (!Validator.isValidAccounting(accounting, config)) return false;
        accounting.setDateTime(LocalDateTime.now());
        store(accounting, DataDestination.STACK);
        exportAccountingCsv();
        return true;
    }

    @Override
    public List<Accounting> getAccountingMovements() {
        return accountingMovements.getAllList();
    }

    @Override
    public double getIncomeTotal() {
        return accountingMovements.getAllList().stream()
                .filter(a -> Accounting.TYPE_INCOME.equals(a.getType()))
                .mapToDouble(Accounting::getAmount)
                .sum();
    }

    @Override
    public double getExpenseTotal() {
        return accountingMovements.getAllList().stream()
                .filter(a -> Accounting.TYPE_EXPENSE.equals(a.getType()))
                .mapToDouble(Accounting::getAmount)
                .sum();
    }

    @Override
    public double getNetTotal() {
        return getIncomeTotal() - getExpenseTotal();
    }

    @Override
    public void exportPersonsCsv() {
        personCsvHandler.save(persons.getAllList());
    }

    @Override
    public void exportProductsCsv() {
        productCsvHandler.save(products.getAllList());
    }

    @Override
    public void exportAccountingCsv() {
        accountingCsvHandler.save(accountingMovements.getAllList());
    }

    private String normalizeDescription(String description) {
        String format = config.get("product.description.format");
        if ("TITLE".equalsIgnoreCase(format)) return toTitleCase(description);
        return description.toUpperCase();
    }

    private String toTitleCase(String text) {
        if (text == null || text.isBlank()) return text;
        String[] words = text.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
