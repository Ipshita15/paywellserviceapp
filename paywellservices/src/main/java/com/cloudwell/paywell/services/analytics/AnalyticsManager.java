package com.cloudwell.paywell.services.analytics;

import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 9/12/18.
 */
public class AnalyticsManager {
    private final static String TAG = "AnalyticsManager";

    /**
     * Sends a screen view with the string resource loaded as its label.
     */
    public static void sendScreenView(int resourceId, String category, String action) {

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(category)
                .putContentType(action));
    }

    /**
     * Sends a screen vie for a screen label.
     */
    public static void sendScreenView(String screenName) {

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(screenName));

        Log.d(TAG, "Screen View recorded: " + screenName);

    }

    public static void sendEvent(String category, String action, String label, long value) {

        Answers.getInstance().logCustom(new CustomEvent(category)
                .putCustomAttribute(AnalyticsParameters.KEY_ACTION, action)
                .putCustomAttribute(AnalyticsParameters.KEY_LABEL, label)
                .putCustomAttribute(AnalyticsParameters.KEY_VALUE, value));

        Log.d(TAG, "Event recorded:");
        Log.d(TAG, "\tCategory: " + category);
        Log.d(TAG, "\tAction: " + action);
        Log.d(TAG, "\tLabel: " + label);
        Log.d(TAG, "\tValue: " + value);

    }

    public static void sendEvent(String category, String action, String label) {

        Answers.getInstance().logCustom(new CustomEvent(category)
                .putCustomAttribute(AnalyticsParameters.KEY_ACTION, action)
                .putCustomAttribute(AnalyticsParameters.KEY_VALUE, label));

        Log.d(TAG, "Event recorded:");
        Log.d(TAG, "\tCategory: " + category);
        Log.d(TAG, "\tAction: " + action);
        Log.d(TAG, "\tLabel: " + label);

    }

    public static void sendEvent(String category, String action) {

        Answers.getInstance().logCustom(new CustomEvent(category)
                .putCustomAttribute(AnalyticsParameters.KEY_ACTION, action));


        Log.d(TAG, "Event recorded:");
        Log.d(TAG, "\tCategory: " + category);
        Log.d(TAG, "\tAction: " + action);
    }


    public static void sendEvent(String category) {

        Answers.getInstance().logCustom(new CustomEvent(category)
                .putCustomAttribute(AnalyticsParameters.KEY_ACTION, category));
        Log.d(TAG, "Event recorded:");
        Log.d(TAG, "\tCategory: " + category);
    }
}

