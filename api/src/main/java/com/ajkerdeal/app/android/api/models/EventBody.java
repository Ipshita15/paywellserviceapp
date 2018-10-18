package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by piash on 12/27/16.
 */

public class EventBody {

    @SerializedName("EventId")
    private String mEventId;
    @SerializedName("MinDiscountPercantage")
    private String mMinDiscountPercantage;
    @SerializedName("MaxDiscountPercantage")
    private String mMaxDiscountPercantage;
    @SerializedName("LowerLimit")
    private String mLowerLimit;
    @SerializedName("Upperlimit")
    private String mUpperlimit;
    @SerializedName("LowestPrice")
    private String mLowestPrice;
    @SerializedName("HighestPrice")
    private String mHighestPrice;
    @SerializedName("CatId")
    private String mCatId;
    @SerializedName("SubCatId")
    private String mSubCatId;
    @SerializedName("SubsubCatId")
    private String mSubsubCatId;
    @SerializedName("OrderBy")
    private String mOrderBy;
    @SerializedName("SortBy")
    private String mSortBy;

    public EventBody(  String mMaxDiscountPercantage, String mMinDiscountPercantage,String mLowerLimit, String mUpperlimit, String mLowestPrice, String mHighestPrice, String mCatId, String mSubCatId, String mSubsubCatId, String mOrderBy, String mSortBy,String mEventId) {
        this.mEventId = mEventId;
        this.mMinDiscountPercantage = mMinDiscountPercantage;
        this.mMaxDiscountPercantage = mMaxDiscountPercantage;
        this.mLowerLimit = mLowerLimit;
        this.mUpperlimit = mUpperlimit;
        this.mLowestPrice = mLowestPrice;
        this.mHighestPrice = mHighestPrice;
        this.mCatId = mCatId;
        this.mSubCatId = mSubCatId;
        this.mSubsubCatId = mSubsubCatId;
        this.mOrderBy = mOrderBy;
        this.mSortBy = mSortBy;
    }

    public String getmEventId() {
        return mEventId;
    }

    public String getmMinDiscountPercantage() {
        return mMinDiscountPercantage;
    }

    public String getmMaxDiscountPercantage() {
        return mMaxDiscountPercantage;
    }

    public String getmLowerLimit() {
        return mLowerLimit;
    }

    public String getmUpperlimit() {
        return mUpperlimit;
    }

    public String getmLowestPrice() {
        return mLowestPrice;
    }

    public String getmHighestPrice() {
        return mHighestPrice;
    }

    public String getmCatId() {
        return mCatId;
    }

    public String getmSubCatId() {
        return mSubCatId;
    }

    public String getmSubsubCatId() {
        return mSubsubCatId;
    }

    public String getmOrderBy() {
        return mOrderBy;
    }

    public String getmSortBy() {
        return mSortBy;
    }

    @Override
    public String toString() {
        return "EventBody{" +
                "mEventId='" + mEventId + '\'' +
                ", mMinDiscountPercantage='" + mMinDiscountPercantage + '\'' +
                ", mMaxDiscountPercantage='" + mMaxDiscountPercantage + '\'' +
                ", mLowerLimit='" + mLowerLimit + '\'' +
                ", mUpperlimit='" + mUpperlimit + '\'' +
                ", mLowestPrice='" + mLowestPrice + '\'' +
                ", mHighestPrice='" + mHighestPrice + '\'' +
                ", mCatId='" + mCatId + '\'' +
                ", mSubCatId='" + mSubCatId + '\'' +
                ", mSubsubCatId='" + mSubsubCatId + '\'' +
                ", mOrderBy='" + mOrderBy + '\'' +
                ", mSortBy='" + mSortBy + '\'' +
                '}';
    }
}
