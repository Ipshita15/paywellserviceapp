package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MhRaju on 1/12/2017.
 */

public class EventCategoryWiseDealsCount {

    @SerializedName("TotalDeals")
    private int mTotalDeals;

    @SerializedName("CategoryId")
    private int mCategoryId;

    @SerializedName("EventCategoryName")
    private String mEventCategoryName;

    @SerializedName("EventCategoryLink")
    private String mEventCategoryLink;

    @SerializedName("SubCategoryId")
    private int mSubCategoryId;

    @SerializedName("SubSubCategoryId")
    private int mSubSubCategoryId;

    @SerializedName("Percentage")
    private int mPercentage;

    @SerializedName("EventId")
    private int mEventId;


    public EventCategoryWiseDealsCount(int mTotalDeals, int mCategoryId, String mEventCategoryName, String mEventCategoryLink, int mSubCategoryId, int mSubSubCategoryId, int mPercentage, int mEventId) {
        this.mTotalDeals = mTotalDeals;
        this.mCategoryId = mCategoryId;
        this.mEventCategoryName = mEventCategoryName;
        this.mEventCategoryLink = mEventCategoryLink;
        this.mSubCategoryId = mSubCategoryId;
        this.mSubSubCategoryId = mSubSubCategoryId;
        this.mPercentage = mPercentage;
        this.mEventId = mEventId;
    }


    public int getmTotalDeals() {
        return mTotalDeals;
    }

    public int getmCategoryId() {
        return mCategoryId;
    }

    public String getmEventCategoryName() {
        return mEventCategoryName;
    }

    public String getmEventCategoryLink() {
        return mEventCategoryLink;
    }

    public int getmSubCategoryId() {
        return mSubCategoryId;
    }

    public int getmSubSubCategoryId() {
        return mSubSubCategoryId;
    }

    public int getmPercentage() {
        return mPercentage;
    }

    public int getmEventId() {
        return mEventId;
    }

    @Override
    public String toString() {
        return "EventCategoryWiseDealsCount{" +
                "mTotalDeals=" + mTotalDeals +
                ", mCategoryId=" + mCategoryId +
                ", mEventCategoryName='" + mEventCategoryName + '\'' +
                ", mEventCategoryLink='" + mEventCategoryLink + '\'' +
                ", mSubCategoryId=" + mSubCategoryId +
                ", mSubSubCategoryId=" + mSubSubCategoryId +
                ", mPercentage=" + mPercentage +
                ", mEventId=" + mEventId +
                '}';
    }
}
