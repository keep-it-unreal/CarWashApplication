package com.carwash.telegram.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UncheckedConversion {

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.size());
        for (Object o : rawCollection) {
            try {
                result.add(clazz.cast(o));
            } catch (ClassCastException e) {
                // log the exception or other error handling
            }
        }
        return result;
    }
}
