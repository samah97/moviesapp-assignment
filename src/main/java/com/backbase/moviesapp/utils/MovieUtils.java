package com.backbase.moviesapp.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class MovieUtils {

    public BigDecimal parseBoxOffice(String boxOffice) {
        if (boxOffice == null || boxOffice.equalsIgnoreCase("N/A")) {
            return BigDecimal.ZERO;
        }

        String cleaned = boxOffice.replace("$", "").replace(",", "").trim();
        return new BigDecimal(cleaned);
    }
}
