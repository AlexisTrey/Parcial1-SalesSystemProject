package co.edu.uptc.persistence.file;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.entity.Accounting;
import co.edu.uptc.util.DateFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccountingCsvHandler {
    private final MyFile myFile = new MyFile();
    private final AppConfig config = AppConfig.getInstance();
    private static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm";

    public List<Accounting> load() {
        List<String> lines = myFile.readLines(getFilePath());
        return parseLines(lines);
    }

    public void save(List<Accounting> movements) {
        List<String> lines = buildLines(movements);
        myFile.writeLines(getFilePath(), lines);
    }

    private String getFilePath() {
        String path = config.get("data.path");
        String name = config.get("data.accounting.name");
        return path + name;
    }

    private List<Accounting> parseLines(List<String> lines) {
        List<Accounting> list = new ArrayList<>();
        for (String line : lines) {
            Accounting a = parseLine(line);
            if (a != null)
                list.add(a);
        }
        return list;
    }

    private Accounting parseLine(String line) {
        try {
            String[] parts = line.split(";");
            return buildAccounting(parts);
        } catch (Exception e) {
            return null;
        }
    }

    private Accounting buildAccounting(String[] parts) {
        return new Accounting(
                parts[0],
                parts[1],
                Double.parseDouble(parts[2]),
                parseDateTime(parts[3]));
    }

    private LocalDateTime parseDateTime(String text) {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
    }

    private List<String> buildLines(List<Accounting> movements) {
        List<String> lines = new ArrayList<>();
        for (Accounting a : movements) {
            lines.add(toLine(a));
        }
        return lines;
    }

    private String toLine(Accounting a) {
        return a.getDescription() + ";" + a.getType() + ";" + a.getAmount() + ";"
                + DateFormatter.format(a.getDateTime());
    }
}
