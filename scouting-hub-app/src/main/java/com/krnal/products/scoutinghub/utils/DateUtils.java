package com.krnal.products.scoutinghub.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
        LocalDate currentDate = LocalDate.now();

        if (birthDate.isBefore(currentDate) || birthDate.isEqual(currentDate)) {
            return String.valueOf(Period.between(birthDate, currentDate).getYears());
        } else {
            throw new IllegalArgumentException("Birthdate is in the future");
        }
    }
}
