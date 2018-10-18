package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MhRaju on 1/12/2017.
 */

public class LiveEventsModel {

    @SerializedName("EventId")
    private int mEventId;

    @SerializedName("EventName")
    private String mEventName;

    public LiveEventsModel(int mEventId, String mEventName) {
        this.mEventId = mEventId;
        this.mEventName = mEventName;
    }

    public int getmEventId() {
        return mEventId;
    }

    public String getmEventName() {
        return mEventName;
    }

    @Override
    public String toString() {
        return "LiveEventsModel{" +
                "mEventId=" + mEventId +
                ", mEventName='" + mEventName + '\'' +
                '}';
    }
}
