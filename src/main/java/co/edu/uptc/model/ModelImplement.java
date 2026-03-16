package co.edu.uptc.model;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.entity.Accounting;
import co.edu.uptc.entity.Person;
import co.edu.uptc.entity.Product;
import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.model.util.DoubleList;
import co.edu.uptc.model.util.Queue;
import co.edu.uptc.model.util.Stack;
import co.edu.uptc.util.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ModelImplement implements ModelInterface {

    private final Queue<Person> persons = new Queue<>();
    private final DoubleList<Product> products = new DoubleList<>();
    private final Stack<Accounting> accountingMovements = new Stack<>();

    private int personIdCounter = 1;
    private int productIdCounter = 1;
    private final AppConfig config = AppConfig.getInstance();

    @Override
    public boolean addPerson(String name, String lastName, String gender, LocalDate birthdate) {
        if (!isValidPerson(name, lastName, gender, birthdate))
            return false;
        persons.enqueue(buildPerson(name, lastName, gender, birthdate));
        return true;
    }

    @Override
    public List<Person> getPersons() {
        return persons.getAllElements();
    }

    @Override
    public boolean addProduct(String description, String unit, double price) {
        if (!isValidProduct(description, unit, price))
            return false;
        products.addLast(buildProduct(description, unit, price));
        return true;
    }

    @Override
    public List<Product> getProducts() {
        return products.getAllList();
    }

    @Override
    public boolean addAccounting(String description, String type, double amount) {
        if (!isValidAccounting(description, type, amount))
            return false;
        accountingMovements.push(buildAccounting(description, type, amount));
        return true;
    }

    @Override
    public List<Accounting> getAccountingMovements() {
        return accountingMovements.getAllElements();
    }

    @Override
    public double getAccountingTotal() {
        return calculateTotal();
    }

    private Person buildPerson(String name, String lastName, String gender, LocalDate birthdate) {
        return new Person(personIdCounter++, name, lastName, gender, birthdate);
    }

    private Product buildProduct(String description, String unit, double price) {
        return new Product(productIdCounter++, description, unit, price);
    }

    private Accounting buildAccounting(String description, String type, double amount) {
        return new Accounting(description, type, amount, LocalDateTime.now());
    }

    private boolean isValidPerson(String name, String lastName, String gender, LocalDate birthdate) {
        return isValidName(name) && isValidLastName(lastName) && isValidGender(gender) && birthdate != null;
    }

    private boolean isValidName(String name) {
        int min = config.getInt("person.name.min", 2);
        int max = config.getInt("person.name.max", 30);
        return Validator.isValidLength(name, min, max);
    }

    private boolean isValidLastName(String lastName) {
        int min = config.getInt("person.lastname.min", 2);
        int max = config.getInt("person.lastname.max", 30);
        return Validator.isValidLength(lastName, min, max);
    }

    private boolean isValidGender(String gender) {
        return Validator.isNotBlank(gender);
    }

    private boolean isValidProduct(String description, String unit, double price) {
        return isValidDescription(description) && Validator.isNotBlank(unit) && isValidPrice(price);
    }

    private boolean isValidDescription(String description) {
        int min = config.getInt("product.description.min", 3);
        int max = config.getInt("product.description.max", 50);
        return Validator.isUpperCase(description) && Validator.isValidLength(description, min, max);
    }

    private boolean isValidPrice(double price) {
        double max = config.getDouble("product.price.max", 999999.99);
        return Validator.isPositive(price) && price <= max;
    }

    private boolean isValidAccounting(String description, String type, double amount) {
        int min = config.getInt("accounting.description.min", 3);
        int max = config.getInt("accounting.description.max", 100);
        return Validator.isValidLength(description, min, max)
                && Validator.isNotBlank(type)
                && Validator.isPositive(amount);
    }

    private double calculateTotal() {
        double total = 0;
        for (Accounting a : accountingMovements.getAllElements()) {
            total += getAmountWithSign(a);
        }
        return total;
    }

    private double getAmountWithSign(Accounting a) {
        return "income".equals(a.getType()) ? a.getAmount() : -a.getAmount();
    }

}
