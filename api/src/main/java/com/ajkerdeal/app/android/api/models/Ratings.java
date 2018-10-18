package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rasel on 6/20/2016.
 */
public class Ratings {

    @SerializedName("Rating")
    int Rating;

    @SerializedName("CountRating")
    int CountRating;

    @SerializedName("TotalCount")
    int TotalCount;


    public int getRating() {
        return Rating;
    }

    public int getCountRating() {
        return CountRating;
    }

    public int getTotalCount() {
        return TotalCount;
    }
}
