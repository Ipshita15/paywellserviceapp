package com.cloudwell.paywell.services.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-05-23.
 */
public class CountryUtility {


    public static String getCountryCode(String countryCode) {

        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();
        Locale locale;
        String name = "";

        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code

            if (code.toLowerCase().equals(countryCode.toLowerCase())) {
                locale = new Locale("", code);
                name = locale.getDisplayCountry();
                break;
            }

        }

        return name;


    }

}
