package com.cloudwell.paywell.services.analytics;

import android.os.Bundle;

import com.cloudwell.paywell.services.app.AppController;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 9/12/18.
 */
public class AnalyticsManager {
    private final static String TAG = "AnalyticsManager";


    /**
     * Sends a screen vie for a screen label.
     */
    public static void sendScreenView(String screenName) {

        FirebaseAnalytics instance = FirebaseAnalytics.getInstance(AppController.getContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT, screenName);
        instance.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    public static void sendEvent(String category, String action, String label, long value) {

//        Answers.getInstance().logCustom(new CustomEvent(category)
//                .putCustomAttribute(AnalyticsParameters.KEY_ACTION, action)
//                .putCustomAttribute(AnalyticsParameters.KEY_LABEL, label)
//                .putCustomAttribute(AnalyticsParameters.KEY_VALUE, value));

        FirebaseAnalytics instance = FirebaseAnalytics.getInstance(AppController.getContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT, category);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, action);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, label);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, value);
        instance.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


    }

    public static void sendEvent(String category, String action, String label) {

        FirebaseAnalytics instance = FirebaseAnalytics.getInstance(AppController.getContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT, category);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, action);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, label);
        instance.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    public static void sendEvent(String category, String action) {

        FirebaseAnalytics instance = FirebaseAnalytics.getInstance(AppController.getContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT, category);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, action);
        instance.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }



}

