package ru.edu.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateConverter {

    public static Instant StringToInstant (String stringDate) {

        Instant instant;

        String europeanDatePattern = "dd.MM.yyyy";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);
        LocalDate localDate = LocalDate.parse(stringDate,europeanDateFormatter);
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        instant = instant.plus(3, ChronoUnit.HOURS);

        return instant;
    }

    public static String InstantToString (Instant instant) {

        String PATTERN_FORMAT = "dd.MM.yyyy";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.of("Europe/Moscow"));

        //данные берем с сервера , а там уже на 3 часа меньше, чем надо
        //instant = instant.minus(3, ChronoUnit.HOURS);
        String formattedInstant = formatter.format(instant);

        return(formattedInstant);
    }

    public static Instant FullStringToInstant (String stringDate) {

        String pattern = "dd.MM.yyyy HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.GERMANY);
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateTimeFormatter);
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();

        instant = instant.plus(3, ChronoUnit.HOURS);


        return instant;
    }

    public static String InstantToFullString (Instant instant) {

        String PATTERN_FORMAT = "dd.MM.yyyy HH:mm:ss";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                    .withZone(ZoneId.of("Europe/Moscow"));

        //данные берем с сервера , а там уже на 3 часа меньше, чем надо
        //instant = instant.minus(3, ChronoUnit.HOURS);
        String formattedInstant = formatter.format(instant);

        return(formattedInstant);
    }





}
