package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS on 6/26/2016.
 */
public class Comment {


    @SerializedName("Rating")
    public int rating;
    @SerializedName("Comments")
    public String comments;
    @SerializedName("RaterId")
    public int raterID;
    @SerializedName("CName")
    public String customerName;


    public String getCustomerName() {
        return customerName;
    }

    public String getComments() {
        return comments;
    }

    public int getRating() {
        return rating;
    }

    public int getRaterID() {
        return raterID;
    }
}
