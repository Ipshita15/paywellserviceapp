package com.cloudwell.paywell.services.utils;

import java.util.UUID;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-11-24.
 */
public class UniqueKeyGenerator {

    private static final int MAX_LENGTH = 16;



    public static String getRandomIntegerBetweenRange(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getUniqueKey(String rid){
        return "AND"+System.currentTimeMillis() + getRandomIntegerBetweenRange()+rid;

    }

}
