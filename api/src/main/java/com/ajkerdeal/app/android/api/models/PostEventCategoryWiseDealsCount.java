package com.ajkerdeal.app.android.api.models;

/**
 * Created by MhRaju on 1/12/2017.
 */

public class PostEventCategoryWiseDealsCount {

    private int EventId;
    private int Percentage;

    public PostEventCategoryWiseDealsCount(int mEventId, int mPercentage) {
        this.EventId = mEventId;
        this.Percentage = mPercentage;
    }

    public int getmEventId() {
        return EventId;
    }

    public int getmPercentage() {
        return Percentage;
    }

    @Override
    public String toString() {
        return "PostEventCategoryWiseDealsCount{" +
                "EventId=" + EventId +
                ", Percentage=" + Percentage +
                '}';
    }
}

