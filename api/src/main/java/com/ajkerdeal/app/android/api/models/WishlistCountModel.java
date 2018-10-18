package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by piash on 6/19/16.
 */
public class WishlistCountModel {

    @SerializedName("Count")
    int count ;

    public int getCount() {
        return count;
    }
}
