package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/20/16.
 */
public class SearchCountResponseModel {

    @SerializedName("Count")
    private int Count;

    public SearchCountResponseModel(int count) {
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    @Override
    public String toString() {
        return "SearchCountResponseModel{" +
                "Count=" + Count +
                '}';
    }
}
