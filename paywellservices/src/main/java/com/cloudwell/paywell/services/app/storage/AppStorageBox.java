package com.cloudwell.paywell.services.app.storage;

import android.content.Context;

import com.orhanobut.hawk.Hawk;


/**
 * Created by Kazi Md. Saidul on 12/27/2017.
 * Spring Rain IT
 * kazis@springrainit.com
 */
public class AppStorageBox {

    /**
     * Enum representing your setting names or key for your setting.
     */
    public enum Key {
        /* Recommended naming convention:
         * ints, floats, doubles, longs:
         * SAMPLE_NUM or SAMPLE_COUNT or SAMPLE_INT, SAMPLE_LONG etc.
         *
         * boolean: IS_SAMPLE, HAS_SAMPLE, CONTAINS_SAMPLE
         *
         * String: SAMPLE_KEY, SAMPLE_STR or just SAMPLE
         */
        AUTHORIZATION_DATA, NOTIFICATION_DETAILS, FARE_DATA, COUNTER_PASSENGER, ResposeAirPriceSearch, ShortDepartArriveTime, AIRTRICKET_EDIT_PASSENGER, SELETED_PASSENGER, AIRPORT, Airline_details, REQUEST_AIR_SERACH


    }

    public static void put(Context context, Key key, Object value) {
        Hawk.init(context).build();
        Hawk.put(key.name(), value);
        Hawk.destroy();
    }

    public static Object get(Context context, Key key) {
        Hawk.init(context).build();
        Object data = Hawk.get(key.name());
        Hawk.destroy();
        return data;
    }

}
