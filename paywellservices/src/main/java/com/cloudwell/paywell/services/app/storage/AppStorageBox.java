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
        SERACH_ID, Request_ID, AUTHORIZATION_DATA, NOTIFICATION_DETAILS, FARE_DATA, COUNTER_PASSENGER, ResposeAirPriceSearch, ShortDepartArriveTime, AIRTRICKET_EDIT_PASSENGER, SELETED_PASSENGER_IDS, AIRPORT, Airline_details, REQUEST_AIR_SERACH, orignAirportAnddestinationairportCode, totalJourney_time, humanReadAbleDate, AIR_PRE_BOOKKING, AIR_BOOKKING, AIRTICKET_BOOKING_RESPONSE, BOOKING_STATUS_ITEM, FROM_CACHE, TO_CACHE, DEPART_DATE, DEPART_DATE_API_formate, CLASS_TYPE, INFANT_PSNGER, KID_PSNGER, ADUL_PSNGER, FROM_CACHE_INDIA, TO_CACHE_INDIA, DEPART_DATE_INDIA, DEPART_DATE_API_formate_INDIA, CLASS_TYPE_INDIA, INFANT_PSNGER_INDIA, KID_PSNGER_INDIA, ADUL_PSNGER_INDIA, FROM_CACHE_ROUND, TO_CACHE_ROUND, CLASS_TYPE_ROUND, INFANT_PSNGER_ROUND, KID_PSNGER_ROUND, ADUL_PSNGER_ROUND, DEPART_DATE_FIRST_ROUND, DEPART_DATE_FIRST_ROUND_Human, DEPART_DATE_SECOUND_ROUND, DEPART_DATE_SECOUND_ROUND_hyuman, REQUEST_API_reschedule, SECURITY_TOKEN, ACCESS_KEY, TRANSPORT_ID, DEPARTURE_ID


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
