package co.edu.uptc.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatter {
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm";

    public static String format(LocalDate date) {
        if (date == null) return "";
        return date.format(getDateFormatter());
    }

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(getDateTimeFormatter());
    }

    public static LocalDate parse(String text) {
        try {
            return LocalDate.parse(text, getDateFormatter());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(DATE_PATTERN);
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    }
}
