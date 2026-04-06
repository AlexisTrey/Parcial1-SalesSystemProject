package co.edu.uptc.view;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.util.DateUtil;
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
        return new String[]{
                i18n.get("menu.persons.add"),
                i18n.get("menu.persons.retire"),
                i18n.get("menu.persons.list"),
                i18n.get("menu.persons.export")
        };
    }

    @Override
    protected boolean handleOption(String option) {
        switch (option) {
            case "1" -> addPerson();
            case "2" -> retirePerson();
            case "3" -> listPersons();
            case "4" -> exportCsv();
            case "0" -> { return false; }
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
        Person person = new Person(0, name, lastName, gender, birthdate);
        if (presenter.addPerson(person)) {
            showSuccess(i18n.get("menu.persons.success"));
        } else {
            showError(i18n.get("menu.persons.error"));
        }
    }

    private String selectGender() {
        return view.selectFromOptions(i18n.get("field.gender") + ":", new String[]{
                i18n.get("gender.male"),
                i18n.get("gender.female")
        });
    }

    private void retirePerson() {
        Person retired = presenter.retireFromQueue();
        if (retired == null) {
            showError(i18n.get("menu.persons.queue.empty"));
            return;
        }
        showSuccess(i18n.get("menu.persons.retired"));
        printPersonTable(retired);
    }

    private void listPersons() {
        showSuccess(i18n.get("menu.persons.listing"));
        printPaginated(personHeaders(), presenter.getPersonsAsTable(), true);
    }

    private void printPersonTable(Person p) {
        String[][] row = new String[][]{{
                p.getName(),
                p.getLastName(),
                p.getGender(),
                String.valueOf(DateUtil.calculateAge(p.getBirthdate()))
        }};
        TablePrinter.print(personHeaders(), row, true);
    }

    private String[] personHeaders() {
        return new String[]{
                i18n.get("header.name"),
                i18n.get("header.lastname"),
                i18n.get("header.gender"),
                i18n.get("header.age")
        };
    }

    private void exportCsv() {
        presenter.exportPersonsCsv();
        String path = AppConfig.getInstance().get("data.path")
                + AppConfig.getInstance().get("data.persons.name");
        showSuccess(i18n.get("csv.export.info") + " " + path);
    }
}
