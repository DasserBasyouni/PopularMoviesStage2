package com.example.dasser.popular.movies.stage2;

/**
   Created by Dasser on 21-Mar-18.
 */

final class Utils {

    private Utils() {}

    static String getRateFormat(float rate) {
        return rate + "/10";
    }

    static String getDateFormat(String date) {
        return date.substring(0, 4);
    }
}
