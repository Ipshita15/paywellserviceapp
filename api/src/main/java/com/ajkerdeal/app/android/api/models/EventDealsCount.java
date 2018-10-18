package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MhRaju on 1/12/2017.
 */

public class EventDealsCount {

    @SerializedName("Count")
    private int mCount;

    public EventDealsCount(int mCount) {
        this.mCount = mCount;
    }

    public int getmCount() {
        return mCount;
    }
}
