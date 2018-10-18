package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sumaya on 12-Oct-17.
 */

public class NewCategoryModels {
    @SerializedName("CategoryId")
    private  int mCategoryId;
    @SerializedName("SubCategoryId")
    private int mSubCategoryID;
    @SerializedName("SubSubCategoryId")
    private int mSubSubCategoryId;
    @SerializedName("Priority")
    private int mPriority;
    @SerializedName("ImageURL")
    private String  mImageLink;
    @SerializedName("RoutingURL")
    private String mRoutingUrl;
    @SerializedName("BngTitle")
    private String mBanglaTitle;
    @SerializedName("EngTitle")
    private String mEngTitle;
    @SerializedName("ColorCode")
    private String mColorCode;

    public NewCategoryModels() {
    }

    public NewCategoryModels(int mCategoryId, int mSubCategoryID, int mSubSubCategoryId, int mPriority, String mImageLink, String mRoutingUrl, String mBanglaTitle, String mEngTitle, String mColorCode) {
        this.mCategoryId = mCategoryId;
        this.mSubCategoryID = mSubCategoryID;
        this.mSubSubCategoryId = mSubSubCategoryId;
        this.mPriority = mPriority;
        this.mImageLink = mImageLink;
        this.mRoutingUrl = mRoutingUrl;
        this.mBanglaTitle = mBanglaTitle;
        this.mEngTitle = mEngTitle;
        this.mColorCode = mColorCode;
    }

    public int getmCategoryId() {
        return mCategoryId;
    }

    public int getmSubCategoryID() {
        return mSubCategoryID;
    }

    public int getmSubSubCategoryId() {
        return mSubSubCategoryId;
    }

    public int getmPriority() {
        return mPriority;
    }

    public String getmImageLink() {
        return mImageLink.trim();
    }

    public String getmRoutingUrl() {
        return mRoutingUrl;
    }

    public String getmBanglaTitle() {
        return mBanglaTitle;
    }

    public String getmEngTitle() {
        return mEngTitle;
    }

    public String getmColorCode() {
        return mColorCode;
    }

    @Override
    public String toString() {
        return "NewCategoryModels{" +
                "mCategoryId=" + mCategoryId +
                ", mSubCategoryID=" + mSubCategoryID +
                ", mSubSubCategoryId=" + mSubSubCategoryId +
                ", mPriority=" + mPriority +
                ", mImageLink='" + mImageLink + '\'' +
                ", mRoutingUrl='" + mRoutingUrl + '\'' +
                ", mBanglaTitle='" + mBanglaTitle + '\'' +
                ", mEngTitle='" + mEngTitle + '\'' +
                ", mColorCode='" + mColorCode + '\'' +
                '}';
    }
}
