package co.edu.uptc.view;

import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.util.TablePrinter;

import java.time.LocalDate;
import java.util.Scanner;

public class PersonMenu extends BaseMenu {
    private final ConsoleView view;
    private final PresenterInterface presenter;

    public PersonMenu(ConsoleView view, PresenterInterface presenter, Scanner scanner) {
        super(scanner);
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    protected String getTitle() {
        return i18n.get("menu.persons.title");
    }

    @Override
    protected String[] getOptions() {
        return new String[] {
                i18n.get("menu.persons.add"),
                i18n.get("menu.persons.list"),
                i18n.get("menu.persons.export")
        };
    }

    @Override
    protected boolean handleOption(String option) {
        switch (option) {
            case "1" -> addPerson();
            case "2" -> listPersons();
            case "3" -> exportCsv();
            case "0" -> {
                return false;
            }
            default -> showInvalidOption();
        }
        return true;
    }

    private void addPerson() {
        showSuccess(i18n.get("menu.persons.adding"));
        String name = view.readLine(i18n.get("field.name") + ": ");
        String lastName = view.readLine(i18n.get("field.lastname") + ": ");
        String gender = selectGender();
        LocalDate birthdate = view.readDate(i18n.get("field.birthdate") + ": ");
        processAddPerson(name, lastName, gender, birthdate);
    }

    private String selectGender() {
        String[] genderOptions = getGenderOptions();
        return view.selectFromOptions(i18n.get("field.gender") + ":", genderOptions);
    }

    private String[] getGenderOptions() {
        return new String[] {
                i18n.get("gender.male"),
                i18n.get("gender.female")
        };
    }

    private void processAddPerson(String name, String lastName, String gender, LocalDate birthdate) {
        if (presenter.addPerson(name, lastName, gender, birthdate)) {
            showSuccess(i18n.get("menu.persons.success"));
        } else {
            showError(i18n.get("menu.persons.error"));
        }
    }

    private void listPersons() {
        showSuccess(i18n.get("menu.persons.listing"));
        String[] headers = getPersonHeaders();
        String[][] rows = presenter.getPersonsAsTable();
        TablePrinter.print(headers, rows);
    }

    private String[] getPersonHeaders() {
        return new String[] {
                i18n.get("header.id"),
                i18n.get("header.name"),
                i18n.get("header.lastname"),
                i18n.get("header.gender"),
                i18n.get("header.birthdate"),
                "Edad"
        };
    }

    private void exportCsv() {
        showSuccess(i18n.get("csv.export.info"));
    }
}
