package co.edu.uptc.persistence.file;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.entity.Person;
import co.edu.uptc.util.DateFormatter;

import java.util.ArrayList;
import java.util.List;

public class PersonCsvHandler {
    private final MyFile myFile = new MyFile();
    private final AppConfig config = AppConfig.getInstance();

    public List<Person> load() {
        List<String> lines = myFile.readLines(getFilePath());
        return parseLines(lines);
    }

    public void save(List<Person> persons) {
        List<String> lines = buildLines(persons);
        myFile.writeLines(getFilePath(), lines);
    }

    private String getFilePath() {
        String path = config.get("data.path");
        String name = config.get("data.persons.name");
        return path + name;
    }

    private List<Person> parseLines(List<String> lines) {
        List<Person> list = new ArrayList<>();
        for (String line : lines) {
            Person p = parseLine(line);
            if (p != null)
                list.add(p);
        }
        return list;
    }

    private Person parseLine(String line) {
        try {
            String[] parts = line.split(";");
            return buildPerson(parts);
        } catch (Exception e) {
            return null;
        }
    }

    private Person buildPerson(String[] parts) {
        return new Person(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                DateFormatter.parse(parts[4]));
    }

    private List<String> buildLines(List<Person> persons) {
        List<String> lines = new ArrayList<>();
        for (Person p : persons) {
            lines.add(toLine(p));
        }
        return lines;
    }

    private String toLine(Person p) {
        return p.getId() + ";" + p.getName() + ";" + p.getLastName() + ";"
                + p.getGender() + ";" + DateFormatter.format(p.getBirthdate());
    }
}
