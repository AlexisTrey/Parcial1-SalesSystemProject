package co.edu.uptc.util;

import java.time.LocalDate;
import java.time.Period;

public class DateUtil {
    public static int calculateAge(LocalDate birthdate) {
        if (birthdate == null)
            return 0;
        return Period.between(birthdate, LocalDate.now()).getYears();
    }
}
