package co.edu.uptc.persistence.file;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.config.AppLogger;
import co.edu.uptc.config.I18n;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.util.DateFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccountingCsvHandler {
    private static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm";
    private static final I18n i18n = I18n.getInstance();
    private final MyFile myFile = new MyFile();
    private final AppConfig config = AppConfig.getInstance();

    public void append(Accounting accounting) {
        myFile.appendLine(getFilePath(), toLine(accounting));
    }

    public List<Accounting> load() {
        return parseLines(myFile.readLines(getFilePath()));
    }

    private String getFilePath() {
        return config.get("data.path") + config.get("data.accounting.name");
    }

    private List<Accounting> parseLines(List<String> lines) {
        List<Accounting> list = new ArrayList<>();
        for (String line : lines) {
            Accounting a = parseLine(line);
            if (a != null) list.add(a);
        }
        return list;
    }

    private Accounting parseLine(String line) {
        try {
            String[] parts = line.split(";");
            return buildAccounting(parts);
        } catch (Exception e) {
            AppLogger.warn(AccountingCsvHandler.class, i18n.get("log.invalid.line") + ": [" + line + "]");
            return null;
        }
    }

    private Accounting buildAccounting(String[] parts) {
        LocalDateTime dt = LocalDateTime.parse(parts[3],
                DateTimeFormatter.ofPattern(DATETIME_PATTERN));
        return new Accounting(parts[0], parts[1], Double.parseDouble(parts[2]), dt);
    }

    private String toLine(Accounting a) {
        return a.getDescription() + ";" + a.getType() + ";" + a.getAmount() + ";"
                + DateFormatter.format(a.getDateTime());
    }
}
