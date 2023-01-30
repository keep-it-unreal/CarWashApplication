package com.carwash.telegram.service.util;

import org.junit.Assert;
import org.junit.Test;

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
        }

        Assert.assertEquals(lRez,true);
    }
}
