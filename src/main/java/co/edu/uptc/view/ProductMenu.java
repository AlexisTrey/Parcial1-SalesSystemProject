package co.edu.uptc.view;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.pojo.Product;
import co.edu.uptc.util.TablePrinter;

import java.util.Scanner;

public class ProductMenu extends BaseMenu {
    private final ConsoleView view;
    private final PresenterInterface presenter;

    public ProductMenu(ConsoleView view, PresenterInterface presenter, Scanner scanner) {
        super(scanner);
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    protected String getTitle() {
        return i18n.get("menu.products.title");
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                i18n.get("menu.products.add"),
                i18n.get("menu.products.retire"),
                i18n.get("menu.products.list"),
                i18n.get("menu.products.export")
        };
    }

    @Override
    protected boolean handleOption(String option) {
        switch (option) {
            case "1" -> addProduct();
            case "2" -> retireProduct();
            case "3" -> listProducts();
            case "4" -> exportCsv();
            case "0" -> { return false; }
            default -> showInvalidOption();
        }
        return true;
    }

    private void addProduct() {
        showSuccess(i18n.get("menu.products.adding"));
        String description = view.readLine(i18n.get("field.description") + ": ");
        String unit = selectUnit();
        double price = view.readPositiveDouble(i18n.get("field.price") + ": ");
        Product product = new Product(0, description, unit, price);
        if (presenter.addProduct(product)) {
            showSuccess(i18n.get("menu.products.success"));
        } else {
            showError(i18n.get("menu.products.error"));
        }
    }

    private String selectUnit() {
        return view.selectFromOptions(i18n.get("field.unit") + ":", new String[]{
                i18n.get("unit.pound"),
                i18n.get("unit.kilo"),
                i18n.get("unit.bale"),
                i18n.get("unit.ton")
        });
    }

    private void retireProduct() {
        String[][] rows = presenter.getProductsAsTable();
        if (rows.length == 0) {
            showError(i18n.get("list.empty"));
            return;
        }
        TablePrinter.print(productHeaders(), rows);
        int id = view.readPositiveInt(i18n.get("field.id.retire") + ": ");
        Product retired = presenter.retireFromList(id);
        if (retired == null) {
            showError(i18n.get("menu.products.not.found"));
            return;
        }
        showSuccess(i18n.get("menu.products.retired"));
        printProductRow(retired);
    }

    private void listProducts() {
        showSuccess(i18n.get("menu.products.listing"));
        printPaginated(productHeaders(), presenter.getProductsAsTable());
    }

    private void printProductRow(Product p) {
        String[][] row = new String[][]{{
                String.valueOf(p.getId()),
                p.getDescription(),
                p.getUnit(),
                String.format("%.2f", p.getPrice())
        }};
        TablePrinter.print(productHeaders(), row);
    }

    private String[] productHeaders() {
        return new String[]{
                i18n.get("header.id"),
                i18n.get("header.description"),
                i18n.get("header.unit"),
                i18n.get("header.price")
        };
    }

    private void exportCsv() {
        presenter.exportProductsCsv();
        String path = AppConfig.getInstance().get("data.path")
                + AppConfig.getInstance().get("data.products.name");
        showSuccess(i18n.get("csv.export.success") + " " + path);
    }
}
