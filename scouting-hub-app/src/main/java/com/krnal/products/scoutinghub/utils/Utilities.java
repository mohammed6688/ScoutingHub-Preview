package com.krnal.products.scoutinghub.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utilities {
    public static String createLogMessage(String... logData) {
        if (logData == null || logData.length < 3) {
            throw new IllegalArgumentException("logData array must have at least 3 elements");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("c = ").append(logData[0]).append(", ");
        sb.append("m = ").append(logData[1]).append(", ");
        sb.append("a = ").append(logData[2]).append(", ");

        for (int i = 3; i < logData.length; i += 2) {
            if (i < logData.length - 2) {  // Check for at least two more elements (key-value pair)
                sb.append(logData[i]).append("='").append(logData[i + 1]).append("', ");
            } else {  // Last key-value pair, no trailing comma
                sb.append(logData[i]).append("='").append(logData[i + 1]).append("'");
            }
        }

        return sb.toString();
    }

    public static String calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
        LocalDate currentDate = LocalDate.now();

        if (birthDate != null && (birthDate.isBefore(currentDate) || birthDate.isEqual(currentDate))) {
            return String.valueOf(Period.between(birthDate, currentDate).getYears());
        } else {
            throw new IllegalArgumentException("Birthdate is in the future");
        }
    }

    public static String convertToCamelCase(String factorName) {
        String[] parts = factorName.toLowerCase().split(" ");
        StringBuilder camelCaseString = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return camelCaseString.toString();
    }

}
