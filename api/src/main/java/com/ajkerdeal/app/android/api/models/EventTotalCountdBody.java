package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by piash on 12/29/16.
 */

public class EventTotalCountdBody {

    @SerializedName("MaxDiscountPercantage")
    private String mMaxDiscountPercantage;
    @SerializedName("MinDiscountPercantage")
    private String mMinDiscountPercantage;
    @SerializedName("LowestPrice")
    private String mLowestPrice;
    @SerializedName("HighestPrice")
    private String mHighestPrice;
    @SerializedName("EventId")
    private String mEventId;

    public EventTotalCountdBody(String mMaxDiscountPercantage, String mMinDiscountPercantage, String mLowestPrice, String mHighestPrice, String mEventId) {
        this.mMaxDiscountPercantage = mMaxDiscountPercantage;
        this.mMinDiscountPercantage = mMinDiscountPercantage;
        this.mLowestPrice = mLowestPrice;
        this.mHighestPrice = mHighestPrice;
        this.mEventId = mEventId;
    }

    public String getmMaxDiscountPercantage() {
        return mMaxDiscountPercantage;
    }

    public String getmMinDiscountPercantage() {
        return mMinDiscountPercantage;
    }

    public String getmLowestPrice() {
        return mLowestPrice;
    }

    public String getmHighestPrice() {
        return mHighestPrice;
    }

    public String getmEventId() {
        return mEventId;
    }

    @Override
    public String toString() {
        return "EventTotalCountdBody{" +
                "mMaxDiscountPercantage='" + mMaxDiscountPercantage + '\'' +
                ", mMinDiscountPercantage='" + mMinDiscountPercantage + '\'' +
                ", mLowestPrice='" + mLowestPrice + '\'' +
                ", mHighestPrice='" + mHighestPrice + '\'' +
                ", mEventId='" + mEventId + '\'' +
                '}';
    }
}
