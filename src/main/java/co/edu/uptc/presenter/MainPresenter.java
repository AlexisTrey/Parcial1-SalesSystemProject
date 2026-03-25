package co.edu.uptc.presenter;

import co.edu.uptc.config.I18n;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;
import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;
import co.edu.uptc.util.DateFormatter;
import co.edu.uptc.util.DateUtil;

import java.util.List;

public class MainPresenter implements PresenterInterface {

    private ModelInterface model;
    private ViewInterface view;
    private final I18n i18n = I18n.getInstance();

    @Override
    public void setModel(ModelInterface model) { this.model = model; }

    @Override
    public void setView(ViewInterface view) { this.view = view; }

    @Override
    public boolean addPerson(Person person) {
        return model.addPerson(person);
    }

    @Override
    public Person retireFromQueue() {
        return model.retireFromQueue();
    }

    @Override
    public String[][] getPersonsAsTable() {
        List<Person> list = model.getPersons();
        String[][] table = new String[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            table[i] = buildPersonRow(list.get(i));
        }
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
        return model.addProduct(product);
    }

    @Override
    public Product retireFromList(int id) {
        return model.retireFromList(id);
    }

    @Override
    public String[][] getProductsAsTable() {
        List<Product> list = model.getProducts();
        String[][] table = new String[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            table[i] = buildProductRow(list.get(i));
        }
        return table;
    }

    private String[] buildProductRow(Product p) {
        return new String[]{
                String.valueOf(p.getId()),
                p.getDescription(),
                p.getUnit(),
                String.format("%.2f", p.getPrice())
        };
    }

    @Override
    public boolean addAccounting(Accounting accounting) {
        return model.addAccounting(accounting);
    }

    @Override
    public List<Accounting> getAccountingMovements() {
        return model.getAccountingMovements();
    }

    @Override
    public void exportPersonsCsv() { model.exportPersonsCsv(); }

    @Override
    public void exportProductsCsv() { model.exportProductsCsv(); }

    @Override
    public void exportAccountingCsv() { model.exportAccountingCsv(); }

}
