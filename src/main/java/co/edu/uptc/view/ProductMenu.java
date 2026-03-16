package co.edu.uptc.view;

import co.edu.uptc.interfaces.PresenterInterface;
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
        return new String[] {
                i18n.get("menu.products.add"),
                i18n.get("menu.products.list"),
                i18n.get("menu.products.export")
        };
    }

    @Override
    protected boolean handleOption(String option) {
        switch (option) {
            case "1" -> addProduct();
            case "2" -> listProducts();
            case "3" -> exportCsv();
            case "0" -> {
                return false;
            }
            default -> showInvalidOption();
        }
        return true;
    }

    private void addProduct() {
        showSuccess(i18n.get("menu.products.adding"));
        String description = readDescription();
        String unit = selectUnit();
        double price = view.readPositiveDouble(i18n.get("field.price") + ": ");
        processAddProduct(description, unit, price);
    }

    private String readDescription() {
        String desc = view.readLine(i18n.get("field.description") + " (MAYUSCULAS): ");
        return desc.toUpperCase();
    }

    private String selectUnit() {
        String[] unitOptions = getUnitOptions();
        return view.selectFromOptions(i18n.get("field.unit") + ":", unitOptions);
    }

    private String[] getUnitOptions() {
        return new String[] {
                i18n.get("unit.pound"),
                i18n.get("unit.kilo"),
                i18n.get("unit.bale"),
                i18n.get("unit.ton"),
                i18n.get("unit.unit")
        };
    }

    private void processAddProduct(String description, String unit, double price) {
        if (presenter.addProduct(description, unit, price)) {
            showSuccess(i18n.get("menu.products.success"));
        } else {
            showError(i18n.get("menu.products.error"));
        }
    }

    private void listProducts() {
        showSuccess(i18n.get("menu.products.listing"));
        String[] headers = getProductHeaders();
        String[][] rows = presenter.getProductsAsTable();
        TablePrinter.print(headers, rows);
    }

    private String[] getProductHeaders() {
        return new String[] {
                i18n.get("header.id"),
                i18n.get("header.description"),
                i18n.get("header.unit"),
                i18n.get("header.price")
        };
    }

    private void exportCsv() {
        showSuccess(i18n.get("csv.export.info"));
    }
}
