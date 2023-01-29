package com.carwash.telegram.service.util;

import com.carwash.telegram.util.DateConverter;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegExpTest {

    @Test
    public void ValidatePriceTest() {

        String text="250.00";
        Boolean lRez = true;

        if (text == null || text.equals("")) {
            lRez = false;
        }

        Pattern pattern = Pattern.compile("^\\d{1,}(\\.d{2})?$");
//        Pattern pattern = Pattern.compile("^[\\d]+(\\.[\\d]+)?$");
//        Pattern pattern = Pattern.compile("250");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            lRez = true;
        } else {
            lRez = false;
        }

        Assert.assertEquals(lRez,true);
    }

    @Test
    public void ValidatePeriodTest() {

        String text = "01.01.2023 31.01.2023";
        Boolean lRez = true;

        if (text == null || text.equals("")) {
            lRez = false;
        }

        //Pattern pattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.d{4}\\s\\d{2}\\.\\d{2}\\.d{4}$");
        Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {

            String[] parts = text.split(" ");
            try {
                Instant iBegin = DateConverter.StringToInstant(parts[0]);
                Instant iEnd = DateConverter.StringToInstant(parts[1]);

                //if (Instant.now().isAfter(iBegin) & Instant.now().isAfter(iEnd) & iEnd.isAfter(iBegin) ) {
                if (iEnd.isAfter(iBegin) ) {
                    lRez = true;
                }

            } catch (Exception ex) {
                lRez = false;
            }
        } else {
            lRez = false;
        }

        Assert.assertEquals(lRez,true);
    }
}
