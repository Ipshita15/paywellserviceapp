package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rasel on 7/13/2016.
 */
public class ReviewResposneModel {
    @SerializedName("Count")
    int responseFromReview;

    public int getResponseFromReview() {
        return responseFromReview;

    }
}
