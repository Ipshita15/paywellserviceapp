package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by has9 on 4/2/16.
 */
public class CountModel {
    @SerializedName("Count")
    private int Count;

    public CountModel(int count) {
        Count = count;
    }

    public int getCount() {
        return Count;
    }
}
