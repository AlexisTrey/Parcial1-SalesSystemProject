package co.edu.uptc.presenter;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.config.AppLogger;
import co.edu.uptc.config.I18n;
import co.edu.uptc.model.util.DoubleList;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;
import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;
import co.edu.uptc.util.DateFormatter;
import co.edu.uptc.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MainPresenter implements PresenterInterface {

    private ModelInterface model;
    private final I18n i18n = I18n.getInstance();

    private final DoubleList<Person> persons = new DoubleList<>();
    private final DoubleList<Product> products = new DoubleList<>();
    private final DoubleList<Accounting> accounting = new DoubleList<>();

    private int personIdCounter  = 1;
    private int productIdCounter = 1;

    private final BiConsumer<Person, DoubleList<Person>> enqueuePerson = (p, q) -> q.addLast(p);
    private final BiConsumer<Product, DoubleList<Product>> enqueueProduct = (p, q) -> q.addLast(p);
    private final BiConsumer<Accounting, DoubleList<Accounting>> pushAccounting = (a, s) -> s.addFirst(a);
    private final Function<DoubleList<Person>, Person> dequeuePerson = DoubleList::removeFirst;
    private final Function<DoubleList<Product>, Product> dequeueProduct = DoubleList::removeFirst;
    private final Function<DoubleList<Accounting>, Accounting> popAccounting = DoubleList::removeFirst;

    @Override
    public void setModel(ModelInterface model) {
        this.model = model;
        initData();
    }

    @Override
    public void setView(ViewInterface view) {}

    private void initData() {
        initPersons();
        initProducts();
        initAccounting();
    }

    private void initPersons() {
        List<Person> loaded = model.loadPersons();
        personIdCounter = loaded.stream().mapToInt(Person::getId).max().orElse(0) + 1;

        for (Person p : loaded) {
            enqueuePerson.accept(p, persons);
        }
    }

    private void initProducts() {
        List<Product> loaded = model.loadProducts();
        productIdCounter = loaded.stream().mapToInt(Product::getId).max().orElse(0) + 1;

        for (Product p : loaded) {
            enqueueProduct.accept(p, products);
        }
    }

    private void initAccounting() {
        List<Accounting> loaded = model.loadAccounting();

        for (int i = loaded.size() - 1; i >= 0; i--) {
            pushAccounting.accept(loaded.get(i), accounting);
        }
    }

    @Override
    public boolean addPerson(Person person) {
        if (!model.validatePerson(person)) {
            AppLogger.warn(MainPresenter.class, i18n.get("log.validation.person")
                    + ": " + person.getName() + " " + person.getLastName());
            return false;
        }

        person.setId(personIdCounter++);
        person.setName(person.getName().trim());
        person.setLastName(person.getLastName().trim());

        enqueuePerson.accept(person, persons);

        model.savePersons(persons.getAllList());
        return true;
    }

    @Override
    public Person retireFromQueue() {
        Person retired = dequeuePerson.apply(persons);
        if (retired != null) model.savePersons(persons.getAllList());
        return retired;
    }

    @Override
    public String[][] getPersonsAsTable() {
        List<Person> list = persons.getAllList();
        String[][] table = new String[list.size()][4];
        for (int i = 0; i < list.size(); i++) table[i] = buildPersonRow(list.get(i));
        return table;
    }

    private String[] buildPersonRow(Person p) {
        return new String[]{
                p.getName(),
                p.getLastName(),
                p.getGender(),
                String.valueOf(DateUtil.calculateAge(p.getBirthdate()))
        };
    }

    @Override
    public boolean addProduct(Product product) {
        if (!model.validateProduct(product)) {
            AppLogger.warn(MainPresenter.class, i18n.get("log.validation.product")
                    + ": " + product.getDescription());
            return false;
        }
        product.setId(productIdCounter++);
        product.setDescription(normalizeDescription(product.getDescription()));
        enqueueProduct.accept(product, products);
        model.saveProducts(products.getAllList());
        return true;
    }

    @Override
    public Product retireFromStack() {
        Product retired = dequeueProduct.apply(products);
        if (retired != null) model.saveProducts(products.getAllList());
        return retired;
    }

    @Override
    public String[][] getProductsAsTable() {
        List<Product> list = products.getAllList();
        String[][] table = new String[list.size()][4];
        for (int i = 0; i < list.size(); i++) table[i] = buildProductRow(list.get(i));
        return table;
    }

    private String[] buildProductRow(Product p) {
        return new String[]{
                String.valueOf(p.getId()), p.getDescription(),
                p.getUnit(), String.format("%.2f", p.getPrice())
        };
    }

    @Override
    public boolean addAccounting(Accounting accounting) {
        if (!model.validateAccounting(accounting)) {
            AppLogger.warn(MainPresenter.class, i18n.get("log.validation.accounting")
                    + ": " + accounting.getDescription());
            return false;
        }

        accounting.setDateTime(LocalDateTime.now());

        pushAccounting.accept(accounting, this.accounting);

        model.appendAccounting(accounting);

        return true;
    }

    @Override
    public Accounting removeLastAccounting() {
        Accounting removed = popAccounting.apply(accounting);

        if (removed != null) {
            model.saveAccounting(accounting.getAllList()); // o el método que uses
        }

        return removed;
    }

    @Override
    public List<Accounting> getAccountingMovements() {
        return accounting.getAllList();
    }

    @Override
    public void exportPersonsCsv() {
        model.savePersons(persons.getAllList());
    }

    @Override
    public void exportProductsCsv() {
        model.saveProducts(products.getAllList());
    }

    private String normalizeDescription(String description) {
        String format = AppConfig.getInstance().get("product.description.format");
        if ("TITLE".equalsIgnoreCase(format)) return toTitleCase(description);
        return description.toUpperCase();
    }

    private String toTitleCase(String text) {
        if (text == null || text.isBlank()) return text;
        String[] words = text.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
