package co.edu.uptc.model.util;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.pojo.Accounting;
import co.edu.uptc.pojo.Person;
import co.edu.uptc.pojo.Product;

import java.time.LocalDate;

public class Validator {
    public static boolean isValidPerson(Person p, AppConfig config) {
        int nameMin = config.getInt("person.name.min", 2);
        int nameMax = config.getInt("person.name.max", 30);
        int lastMin = config.getInt("person.lastname.min", 2);
        int lastMax = config.getInt("person.lastname.max", 30);
        return isValidLength(p.getName(), nameMin, nameMax)
                && isValidLength(p.getLastName(), lastMin, lastMax)
                && isNotBlank(p.getGender())
                && isValidBirthdate(p.getBirthdate());
    }

    public static boolean isValidProduct(Product p, AppConfig config) {
        int descMin = config.getInt("product.description.min", 3);
        int descMax = config.getInt("product.description.max", 50);
        double priceMax = config.getDouble("product.price.max", 999999.99);
        return isValidLength(p.getDescription(), descMin, descMax)
                && isNotBlank(p.getUnit())
                && isPositive(p.getPrice())
                && isWithinMax(p.getPrice(), priceMax);
    }

    public static boolean isValidAccounting(Accounting a, AppConfig config) {
        int descMin = config.getInt("accounting.description.min", 3);
        int descMax = config.getInt("accounting.description.max", 100);
        return isValidLength(a.getDescription(), descMin, descMax)
                && isValidType(a.getType())
                && isPositive(a.getAmount());
    }

    public static boolean isValidLength(String value, int min, int max) {
        if (value == null)
            return false;
        return value.trim().length() >= min && value.trim().length() <= max;
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    public static boolean isWithinMax(double value, double max) {
        return value <= max;
    }

    public static boolean isValidBirthdate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    private static boolean isValidType(String type) {
        return Accounting.TYPE_INCOME.equals(type) || Accounting.TYPE_EXPENSE.equals(type);
    }
}
