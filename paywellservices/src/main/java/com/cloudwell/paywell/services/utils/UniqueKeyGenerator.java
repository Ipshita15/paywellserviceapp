package com.cloudwell.paywell.services.utils;

import java.util.Random;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-11-24.
 */
public class UniqueKeyGenerator {

    private static final int MAX_LENGTH = 10;

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static String getUniqueKey(){
        return System.currentTimeMillis() + random();

    }

}
