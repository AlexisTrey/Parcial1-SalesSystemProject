package co.edu.uptc.model.util;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;

import java.time.LocalDate;
import java.util.function.Predicate;

public class Validator {
    private final AppConfig config = AppConfig.getInstance();

    public boolean validate(Person p) {
        Predicate<Person> nameOk   = person -> isLength(person.getName(),     nameMin(), nameMax());
        Predicate<Person> lastOk   = person -> isLength(person.getLastName(), lastMin(), lastMax());
        Predicate<Person> genderOk = person -> notBlank(person.getGender());
        Predicate<Person> dateOk   = person -> validDate(person.getBirthdate());
        return nameOk.and(lastOk).and(genderOk).and(dateOk).test(p);
    }

    public boolean validate(Product p) {
        Predicate<Product> descOk  = prod -> isLength(prod.getDescription(), descMin(), descMax());
        Predicate<Product> unitOk  = prod -> notBlank(prod.getUnit());
        Predicate<Product> priceOk = prod -> prod.getPrice() > 0 && prod.getPrice() <= priceMax();
        return descOk.and(unitOk).and(priceOk).test(p);
    }

    public boolean validate(Accounting a) {
        Predicate<Accounting> descOk   = acc -> isLength(acc.getDescription(), accMin(), accMax());
        Predicate<Accounting> typeOk   = acc -> isValidType(acc.getType());
        Predicate<Accounting> amountOk = acc -> acc.getAmount() > 0;
        return descOk.and(typeOk).and(amountOk).test(a);
    }

    private boolean isLength(String value, int min, int max) {
        if (value == null) return false;
        int len = value.trim().length();
        return len >= min && len <= max;
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    private boolean validDate(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) return false;

        String minDateStr = config.get("person.birthdate.min");
        LocalDate minDate = co.edu.uptc.util.DateFormatter.parse(minDateStr);

        if (minDate == null) return false;

        return !date.isBefore(minDate);
    }

    private boolean isValidType(String type) {
        return Accounting.TYPE_INCOME.equals(type) || Accounting.TYPE_EXPENSE.equals(type);
    }

    private int nameMin()     { return config.getInt("person.name.min",           2); }
    private int nameMax()     { return config.getInt("person.name.max",           30); }
    private int lastMin()     { return config.getInt("person.lastname.min",       2); }
    private int lastMax()     { return config.getInt("person.lastname.max",       30); }
    private int descMin()     { return config.getInt("product.description.min",   3); }
    private int descMax()     { return config.getInt("product.description.max",   50); }
    private double priceMax() { return config.getDouble("product.price.max",      999999.99); }
    private int accMin()      { return config.getInt("accounting.description.min",3); }
    private int accMax()      { return config.getInt("accounting.description.max",100); }
}
