package co.edu.uptc.view;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.util.DateFormatter;
import co.edu.uptc.util.DateUtil;
import co.edu.uptc.view.components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class PersonPanel extends BasePanel {
    private AppField fldName;
    private AppField   fldLastName;
    private AppField   fldBirthdate;
    private AppComboBox<String> cmbGender;

    private AppTable table;
    private PaginationPanel pagination;

    private AppButton btnAdd;
    private AppButton btnRetire;
    private AppButton btnExport;

    public PersonPanel(PresenterInterface presenter) {
        super(presenter);
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(i18n.get("menu.persons.title")), BorderLayout.NORTH);
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

        AppLabel title = new AppLabel(i18n.get("menu.persons.adding"), AppLabel.Style.SECTION);
        card.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(4, 1, 6, 6));
        fields.setBackground(AppColors.SURFACE);

        fldName      = new AppField(i18n.get("field.name"));
        fldLastName  = new AppField(i18n.get("field.lastname"));
        fldBirthdate = new AppField(i18n.get("field.birthdate"));
        cmbGender    = new AppComboBox<>(new String[]{
                i18n.get("gender.male"), i18n.get("gender.female")
        });

        fields.add(buildFormRow("field.name",      fldName));
        fields.add(buildFormRow("field.lastname",  fldLastName));
        fields.add(buildFormRow("field.birthdate", fldBirthdate));
        fields.add(buildFormRow("field.gender",    cmbGender));
        card.add(fields, BorderLayout.CENTER);

        btnAdd    = new AppButton(i18n.get("menu.persons.add"),    AppButton.Style.SUCCESS);
        btnRetire = new AppButton(i18n.get("menu.persons.retire"), AppButton.Style.DANGER);
        btnExport = new AppButton(i18n.get("menu.persons.export"), AppButton.Style.PRIMARY);

        card.add(buildButtonRow(btnAdd, btnRetire, btnExport), BorderLayout.SOUTH);
        addListeners();
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 8));

        AppLabel title = new AppLabel(i18n.get("menu.persons.listing"), AppLabel.Style.SECTION);
        card.add(title, BorderLayout.NORTH);

        table      = new AppTable(getHeaders());
        pagination = new PaginationPanel(table, pageSize);

        card.add(table, BorderLayout.CENTER);
        card.add(pagination, BorderLayout.SOUTH);
        return card;
    }

    private void addListeners() {
        btnAdd.addActionListener(e -> addPerson());
        btnRetire.addActionListener(e -> retirePerson());
        btnExport.addActionListener(e -> exportCsv());
    }

    private void addPerson() {
        String name      = fldName.getValue();
        String lastName  = fldLastName.getValue();
        String gender    = cmbGender.getSelectedValue();
        LocalDate birth  = DateFormatter.parse(fldBirthdate.getValue());

        if (birth == null) {
            showError("error.invalid.date");
            return;
        }

        String minDateStr = AppConfig.getInstance().get("person.birthdate.min");
        LocalDate minDate = DateFormatter.parse(minDateStr);

        if (birth.isBefore(minDate)) {
            showError("error.birthdate.min");
            return;
        }

        Person person = new Person(0, name, lastName, gender, birth);

        if (presenter.addPerson(person)) {
            showSuccess("menu.persons.success");
            clearForm();
            refresh();
        } else {
            showError("menu.persons.error");
        }
    }

    private void retirePerson() {
        Person retired = presenter.retireFromQueue();
        if (retired == null) {
            showError("menu.persons.queue.empty");
            return;
        }
        showSuccess("menu.persons.retired");
        JOptionPane.showMessageDialog(this,
                retired.getName() + " " + retired.getLastName()
                        + " | " + i18n.get("header.age") + ": "
                        + DateUtil.calculateAge(retired.getBirthdate()),
                i18n.get("menu.persons.retired"), JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }

    private void exportCsv() {
        presenter.exportPersonsCsv();
        showSuccess("csv.export.success");
    }

    private void clearForm() {
        fldName.clear();
        fldLastName.clear();
        fldBirthdate.clear();
        cmbGender.setSelectedIndex(0);
    }

    @Override
    public void refresh() {
        pagination.load(presenter.getPersonsAsTable());
    }

    private String[] getHeaders() {
        return new String[]{
                i18n.get("header.name"),     i18n.get("header.lastname"),
                i18n.get("header.gender"),   i18n.get("header.age")
        };
    }
}
