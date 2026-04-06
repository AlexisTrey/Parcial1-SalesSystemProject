package co.edu.uptc.view;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.util.DateFormatter;
import co.edu.uptc.util.TablePrinter;
import co.edu.uptc.util.Utilities;

import java.util.List;
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
        return new String[]{
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
            case "3" -> showFilePath();
            case "0" -> { return false; }
            default -> showInvalidOption();
        }
        return true;
    }

    private void addMovement() {
        showSuccess(i18n.get("menu.accounting.adding"));
        String description = view.readLine(i18n.get("field.description") + ": ");
        String type = selectMovementType();
        double amount = view.readPositiveDouble(i18n.get("field.amount") + ": ");
        Accounting accounting = new Accounting(description, type, amount, null);
        if (presenter.addAccounting(accounting)) {
            showSuccess(i18n.get("menu.accounting.success"));
        } else {
            showError(i18n.get("menu.accounting.error"));
        }
    }

    private String selectMovementType() {
        String incomeLabel  = i18n.get("accounting.type.income");
        String expenseLabel = i18n.get("accounting.type.expense");
        String selected = view.selectFromOptions(i18n.get("field.type") + ":", new String[]{
                incomeLabel, expenseLabel
        });
        return selected.equals(incomeLabel) ? Accounting.TYPE_INCOME : Accounting.TYPE_EXPENSE;
    }

    private void listMovements() {
        List<Accounting> movements = presenter.getAccountingMovements();
        showSuccess(i18n.get("menu.accounting.listing"));
        if (movements.isEmpty()) {
            showInfo(i18n.get("list.empty"));
        } else {
            TablePrinter.print(accountingHeaders(), buildRows(movements));
        }
        printTotals(movements);
    }

    private String[][] buildRows(List<Accounting> movements) {
        String[][] rows = new String[movements.size()][4];
        for (int i = 0; i < movements.size(); i++) rows[i] = buildRow(movements.get(i));
        return rows;
    }

    private String[] buildRow(Accounting a) {
        String label = Accounting.TYPE_INCOME.equals(a.getType())
                ? i18n.get("accounting.type.income")
                : i18n.get("accounting.type.expense");
        return new String[]{
                a.getDescription(), label,
                String.format("%.2f", a.getAmount()),
                DateFormatter.format(a.getDateTime())
        };
    }

    private String[] accountingHeaders() {
        return new String[]{
                i18n.get("header.description"), i18n.get("header.type"),
                i18n.get("header.amount"),      i18n.get("header.datetime")
        };
    }

    private void printTotals(List<Accounting> movements) {
        double income = 0, expense = 0;
        for (Accounting a : movements) {
            if (Accounting.TYPE_INCOME.equals(a.getType())) income += a.getAmount();
            else expense += a.getAmount();
        }
        System.out.println();
        System.out.println(Utilities.CYAN   + "  +" + "-".repeat(38) + "+" + Utilities.RESET);
        System.out.printf( Utilities.GREEN  + "  | %-22s %12.2f |%n" + Utilities.RESET,
                i18n.get("accounting.type.income") + ":", income);
        System.out.printf( Utilities.RED    + "  | %-22s %12.2f |%n" + Utilities.RESET,
                i18n.get("accounting.type.expense") + ":", expense);
        System.out.println(Utilities.CYAN   + "  |" + "-".repeat(38) + "|" + Utilities.RESET);
        System.out.printf( Utilities.YELLOW + "  | %-22s %12.2f |%n" + Utilities.RESET,
                i18n.get("menu.accounting.total") + ":", income - expense);
        System.out.println(Utilities.CYAN   + "  +" + "-".repeat(38) + "+" + Utilities.RESET);
    }

    private void showFilePath() {
        String path = AppConfig.getInstance().get("data.path")
                + AppConfig.getInstance().get("data.accounting.name");
        showSuccess(i18n.get("csv.export.info") + " " + path);
    }
}
