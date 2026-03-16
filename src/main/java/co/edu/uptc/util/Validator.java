package co.edu.uptc.util;

public class Validator {
    public static boolean isValidLength(String value, int min, int max) {
        if (value == null)
            return false;
        return value.length() >= min && value.length() <= max;
    }

    public static boolean isUpperCase(String value) {
        if (value == null || value.isBlank())
            return false;
        return value.equals(value.toUpperCase());
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
