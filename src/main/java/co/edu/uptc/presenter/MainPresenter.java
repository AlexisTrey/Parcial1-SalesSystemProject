package co.edu.uptc.presenter;

import co.edu.uptc.config.I18n;
import co.edu.uptc.entity.Accounting;
import co.edu.uptc.entity.Person;
import co.edu.uptc.entity.Product;
import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;
import co.edu.uptc.persistence.file.AccountingCsvHandler;
import co.edu.uptc.persistence.file.PersonCsvHandler;
import co.edu.uptc.persistence.file.ProductCsvHandler;
import co.edu.uptc.util.DateFormatter;
import co.edu.uptc.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

public class MainPresenter implements PresenterInterface {

    private ModelInterface model;
    private ViewInterface view;

    private final PersonCsvHandler personCsv = new PersonCsvHandler();
    private final ProductCsvHandler productCsv = new ProductCsvHandler();
    private final AccountingCsvHandler accountingCsv = new AccountingCsvHandler();

    @Override
    public void setModel(ModelInterface model) {
        this.model = model;
        loadAllData();
    }

    @Override
    public void setView(ViewInterface view) {
        this.view = view;
    }

    @Override
    public boolean addPerson(String name, String lastName, String gender, LocalDate birthdate) {
        boolean result = model.addPerson(name, lastName, gender, birthdate);
        if (result)
            savePersons();
        return result;
    }

    @Override
    public String[][] getPersonsAsTable() {
        List<Person> list = model.getPersons();
        return buildPersonRows(list);
    }

    @Override
    public boolean addProduct(String description, String unit, double price) {
        boolean result = model.addProduct(description, unit, price);
        if (result)
            saveProducts();
        return result;
    }

    @Override
    public String[][] getProductsAsTable() {
        List<Product> list = model.getProducts();
        return buildProductRows(list);
    }

    @Override
    public boolean addAccounting(String description, String type, double amount) {
        boolean result = model.addAccounting(description, type, amount);
        if (result)
            saveAccounting();
        return result;
    }

    @Override
    public String[][] getAccountingAsTable() {
        List<Accounting> list = model.getAccountingMovements();
        return buildAccountingRows(list);
    }

    @Override
    public double getAccountingTotal() {
        return model.getAccountingTotal();
    }

    private void loadAllData() {
        loadPersons();
        loadProducts();
        loadAccounting();
    }

    private void loadPersons() {
        for (Person p : personCsv.load()) {
            model.addPerson(p.getName(), p.getLastName(), p.getGender(), p.getBirthdate());
        }
    }

    private void loadProducts() {
        for (Product p : productCsv.load()) {
            model.addProduct(p.getDescription(), p.getUnit(), p.getPrice());
        }
    }

    private void loadAccounting() {
        for (Accounting a : accountingCsv.load()) {
            model.addAccounting(a.getDescription(), a.getType(), a.getAmount());
        }
    }

    private void savePersons() {
        personCsv.save(model.getPersons());
    }

    private void saveProducts() {
        productCsv.save(model.getProducts());
    }

    private void saveAccounting() {
        accountingCsv.save(model.getAccountingMovements());
    }

    private String[][] buildPersonRows(List<Person> list) {
        String[][] rows = new String[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            rows[i] = toPersonRow(list.get(i));
        }
        return rows;
    }

    private String[] toPersonRow(Person p) {
        return new String[] {
                String.valueOf(p.getId()),
                p.getName(),
                p.getLastName(),
                p.getGender(),
                DateFormatter.format(p.getBirthdate()),
                String.valueOf(DateUtil.calculateAge(p.getBirthdate()))
        };
    }

    private String[][] buildProductRows(List<Product> list) {
        String[][] rows = new String[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            rows[i] = toProductRow(list.get(i));
        }
        return rows;
    }

    private String[] toProductRow(Product p) {
        return new String[] {
                String.valueOf(p.getId()),
                p.getDescription(),
                p.getUnit(),
                formatPrice(p.getPrice())
        };
    }

    private String[][] buildAccountingRows(List<Accounting> list) {
        String[][] rows = new String[list.size()][4];
        I18n i18n = I18n.getInstance();
        for (int i = 0; i < list.size(); i++) {
            rows[i] = toAccountingRow(list.get(i), i18n);
        }
        return rows;
    }

    private String[] toAccountingRow(Accounting a, I18n i18n) {
        return new String[] {
                a.getDescription(),
                getTypeLabel(a.getType(), i18n),
                formatPrice(a.getAmount()),
                DateFormatter.format(a.getDateTime())
        };
    }

    private String getTypeLabel(String type, I18n i18n) {
        return "income".equals(type)
                ? i18n.get("accounting.type.income")
                : i18n.get("accounting.type.expense");
    }

    private String formatPrice(double price) {
        return String.format("$%.2f", price);
    }

}
