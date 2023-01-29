package ru.edu.util;

import org.junit.Assert;
import org.junit.Test;
import ru.edu.util.DateConverter;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateConverterTest {

    @Test
    public void FullStringToInstantTest() {

        String stringDate = "30.01.2023 18:30:00";
        Instant instant = DateConverter.FullStringToInstant(stringDate);

        String newString = DateConverter.InstantToFullString(instant);
        Assert.assertEquals(stringDate,newString);


    }


}
