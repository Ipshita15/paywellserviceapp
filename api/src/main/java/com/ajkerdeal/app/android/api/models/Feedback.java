package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rasel on 7/13/2016.
 */
public class Feedback {

    public Feedback(int ratingId, String ratingType, int raterId, String raterType, int rating, String ratingFrom, String comments) {
        RatingId = ratingId;
        RatingType = ratingType;
        RaterId = raterId;
        RaterType = raterType;
        Rating = rating;
        Comments = comments;
        RatingFrom = ratingFrom;
    }

    @SerializedName("RatingId")
    int RatingId;


    @SerializedName("RatingType")
    String RatingType;


    @SerializedName("RaterId")
    int RaterId;


    @SerializedName("Customer")
    String RaterType;


    @SerializedName("Rating")
    int Rating;


    @SerializedName("RatingFrom")
    String RatingFrom;


    @SerializedName("Comments")
    String Comments;

    public int getRatingId() {
        return RatingId;
    }

    public String getRatingType() {
        return RatingType;
    }

    public int getRaterId() {
        return RaterId;
    }

    public String getRaterType() {
        return RaterType;
    }

    public int getRating() {
        return Rating;
    }

    public String getComments() {
        return Comments;
    }

    public String getRatingFrom() {
        return RatingFrom;
    }
}
