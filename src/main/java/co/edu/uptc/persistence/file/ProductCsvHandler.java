package co.edu.uptc.persistence.file;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.config.AppLogger;
import co.edu.uptc.config.I18n;
import co.edu.uptc.pojo.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductCsvHandler {
    private static final I18n i18n = I18n.getInstance();
    private final MyFile myFile = new MyFile();
    private final AppConfig config = AppConfig.getInstance();

    public List<Product> load() {
        List<String> lines = myFile.readLines(getFilePath());
        return parseLines(lines);
    }

    public void save(List<Product> products) {
        myFile.writeLines(getFilePath(), buildLines(products));
    }

    private String getFilePath() {
        return config.get("data.path") + config.get("data.products.name");
    }

    private List<Product> parseLines(List<String> lines) {
        List<Product> list = new ArrayList<>();
        for (String line : lines) {
            Product p = parseLine(line);
            if (p != null) list.add(p);
        }
        return list;
    }

    private Product parseLine(String line) {
        try {
            String[] parts = line.split(";");
            return buildProduct(parts);
        } catch (Exception e) {
            AppLogger.warn(ProductCsvHandler.class, i18n.get("log.invalid.line") + ": [" + line + "]");
            return null;
        }
    }

    private Product buildProduct(String[] parts) {
        return new Product(
                Integer.parseInt(parts[0]),
                parts[1], parts[2],
                Double.parseDouble(parts[3]));
    }

    private List<String> buildLines(List<Product> products) {
        List<String> lines = new ArrayList<>();
        for (Product p : products) lines.add(toLine(p));
        return lines;
    }

    private String toLine(Product p) {
        return p.getId() + ";" + p.getDescription() + ";" + p.getUnit() + ";" + p.getPrice();
    }
}
