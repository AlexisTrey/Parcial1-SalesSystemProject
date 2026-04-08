package co.edu.uptc.view;

import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.view.components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AccountingPanel extends BasePanel {
    private AppField fldDescription;
    private AppField fldAmount;
    private AppComboBox<String> cmbType;

    private AppTable table;

    private AppButton btnAdd;
    private AppButton btnExport;
    private AppButton btnRemove;

    private AppLabel lblIncome;
    private AppLabel lblExpense;
    private AppLabel lblBalance;

    public AccountingPanel(PresenterInterface presenter) {
        super(presenter);
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(i18n.get("menu.accounting.title")), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(AppColors.BACKGROUND);
        center.setBorder(new EmptyBorder(14, 16, 14, 16));
        center.add(buildFormCard(), BorderLayout.NORTH);
        center.add(buildTableCard(), BorderLayout.CENTER);
        return center;
    }

    private JPanel buildFormCard() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 10));

        card.add(new AppLabel(i18n.get("menu.accounting.adding"), AppLabel.Style.SECTION), BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(3, 1, 10, 10));
        fields.setBackground(AppColors.SURFACE);

        fldDescription = new AppField(i18n.get("field.description"));
        fldAmount      = new AppField(i18n.get("field.amount"));
        cmbType        = new AppComboBox<>(new String[]{
                i18n.get("accounting.type.income"),
                i18n.get("accounting.type.expense")
        });

        fields.add(buildFormRow("field.description", fldDescription));
        fields.add(buildFormRow("field.type",        cmbType));
        fields.add(buildFormRow("field.amount",      fldAmount));
        card.add(fields, BorderLayout.CENTER);

        btnAdd    = new AppButton(i18n.get("menu.accounting.add"),    AppButton.Style.SUCCESS);
        btnRemove = new AppButton(i18n.get("menu.accounting.remove"), AppButton.Style.DANGER);
        btnExport = new AppButton(i18n.get("menu.accounting.export"), AppButton.Style.PRIMARY);

        card.add(buildButtonRow(btnAdd, btnRemove, btnExport), BorderLayout.SOUTH);
        addListeners();
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 8));

        card.add(new AppLabel(i18n.get("menu.accounting.listing"), AppLabel.Style.SECTION), BorderLayout.NORTH);

        table = new AppTable(getHeaders());
        card.add(table, BorderLayout.CENTER);
        card.add(buildTotalsPanel(), BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildTotalsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 2, 2));
        panel.setBackground(AppColors.SURFACE_ALT);
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));

        lblIncome  = new AppLabel("", AppLabel.Style.SUCCESS);
        lblExpense = new AppLabel("", AppLabel.Style.ERROR);
        lblBalance = new AppLabel("", AppLabel.Style.SECTION);

        panel.add(lblIncome);
        panel.add(lblExpense);
        panel.add(lblBalance);
        return panel;
    }

    private void addListeners() {
        btnAdd.addActionListener(e -> addMovement());
        btnRemove.addActionListener(e -> removeMovement());
        btnExport.addActionListener(e -> exportCsv());
    }

    private void addMovement() {
        String description = fldDescription.getValue();
        String selectedLabel = cmbType.getSelectedValue();
        String type = selectedLabel.equals(i18n.get("accounting.type.income"))
                ? Accounting.TYPE_INCOME : Accounting.TYPE_EXPENSE;
        double amount = parseAmount(fldAmount.getValue());

        if (amount <= 0) { showError("error.positive.value"); return; }

        Accounting accounting = new Accounting(description, type, amount, null);
        if (presenter.addAccounting(accounting)) {
            showSuccess("menu.accounting.success");
            clearForm();
            refresh();
        } else {
            showError("menu.accounting.error");
        }
    }

    private void removeMovement() {
        Accounting removed = presenter.removeLastAccounting();

        if (removed == null) {
            showError("list.empty");
            return;
        }

        showSuccess("menu.accounting.removed");

        JOptionPane.showMessageDialog(this,
                removed.getDescription() + " | $" + String.format("%.2f", removed.getAmount()),
                i18n.get("menu.accounting.removed"),
                JOptionPane.INFORMATION_MESSAGE
        );

        refresh();
    }

    private void exportCsv() {
        showSuccess("csv.export.success");
    }

    private double parseAmount(String value) {
        try {
            return Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void clearForm() {
        fldDescription.clear();
        fldAmount.clear();
        cmbType.setSelectedIndex(0);
    }

    @Override
    public void refresh() {
        List<Accounting> movements = presenter.getAccountingMovements();
        String[][] rows = buildRows(movements);
        table.setData(rows);
        updateTotals(movements);
    }

    private String[][] buildRows(List<Accounting> movements) {
        String[][] rows = new String[movements.size()][4];
        for (int i = 0; i < movements.size(); i++) {
            Accounting a = movements.get(i);
            String label = Accounting.TYPE_INCOME.equals(a.getType())
                    ? i18n.get("accounting.type.income")
                    : i18n.get("accounting.type.expense");
            rows[i] = new String[]{
                    a.getDescription(), label,
                    String.format("%.2f", a.getAmount()),
                    a.getDateTime() != null ? a.getDateTime().toString() : ""
            };
        }
        return rows;
    }

    private void updateTotals(List<Accounting> movements) {
        double income = 0, expense = 0;
        for (Accounting a : movements) {
            if (Accounting.TYPE_INCOME.equals(a.getType())) income += a.getAmount();
            else expense += a.getAmount();
        }
        lblIncome.setSuccess(i18n.get("accounting.type.income") + ": $" + String.format("%.2f", income));
        lblExpense.setError(i18n.get("accounting.type.expense") + ": $" + String.format("%.2f", expense));
        lblBalance.setText(i18n.get("menu.accounting.total") + ": $" + String.format("%.2f", income - expense));
    }

    private String[] getHeaders() {
        return new String[]{
                i18n.get("header.description"), i18n.get("header.type"),
                i18n.get("header.amount"),       i18n.get("header.datetime")
        };
    }
}
