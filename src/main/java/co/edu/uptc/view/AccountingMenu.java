package co.edu.uptc.view;

import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.util.TablePrinter;

import java.util.Scanner;

public class AccountingMenu extends BaseMenu {
    private final ConsoleView view;
    private final PresenterInterface presenter;

    public AccountingMenu(ConsoleView view, PresenterInterface presenter, Scanner scanner) {
        super(scanner);
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    protected String getTitle() {
        return i18n.get("menu.accounting.title");
    }

    @Override
    protected String[] getOptions() {
        return new String[] {
                i18n.get("menu.accounting.add"),
                i18n.get("menu.accounting.list"),
                i18n.get("menu.accounting.export")
        };
    }

    @Override
    protected boolean handleOption(String option) {
        switch (option) {
            case "1" -> addMovement();
            case "2" -> listMovements();
            case "3" -> exportCsv();
            case "0" -> {
                return false;
            }
            default -> showInvalidOption();
        }
        return true;
    }

    private void addMovement() {
        showSuccess(i18n.get("menu.accounting.adding"));
        String description = view.readLine(i18n.get("field.description") + ": ");
        String typeKey = selectMovementType();
        double amount = view.readPositiveDouble(i18n.get("field.amount") + ": ");
        processAddMovement(description, typeKey, amount);
    }

    private String selectMovementType() {
        String[] typeOptions = getTypeOptions();
        String selected = view.selectFromOptions(i18n.get("field.type") + ":", typeOptions);
        return mapTypeToKey(selected, typeOptions);
    }

    private String[] getTypeOptions() {
        return new String[] {
                i18n.get("accounting.type.income"),
                i18n.get("accounting.type.expense")
        };
    }

    private String mapTypeToKey(String selected, String[] options) {
        return selected.equals(options[0]) ? "income" : "expense";
    }

    private void processAddMovement(String description, String typeKey, double amount) {
        if (presenter.addAccounting(description, typeKey, amount)) {
            showSuccess(i18n.get("menu.accounting.success"));
        } else {
            showError(i18n.get("menu.accounting.error"));
        }
    }

    private void listMovements() {
        showSuccess(i18n.get("menu.accounting.listing"));
        String[] headers = getAccountingHeaders();
        String[][] rows = presenter.getAccountingAsTable();
        TablePrinter.print(headers, rows);
        showTotal();
    }

    private String[] getAccountingHeaders() {
        return new String[] {
                i18n.get("header.description"),
                i18n.get("header.type"),
                i18n.get("header.amount"),
                i18n.get("header.datetime")
        };
    }

    private void showTotal() {
        String total = String.format("$%.2f", presenter.getAccountingTotal());
        view.showMessage(i18n.get("menu.accounting.total") + ": " + total);
    }

    private void exportCsv() {
        showSuccess(i18n.get("csv.export.info"));
    }
}
