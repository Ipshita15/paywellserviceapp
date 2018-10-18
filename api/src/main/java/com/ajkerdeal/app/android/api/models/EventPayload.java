package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by piash on 12/27/16.
 */

public class EventPayload {

    @SerializedName("Id")
    private int mId;
    @SerializedName("EventId")
    private int mEventId;
    @SerializedName("CouponPrice")
    private int mCouponPrice;
    @SerializedName("TotalDiscountPercentage")
    private int mTotalDiscountPercentage;
    @SerializedName("AdDiscountPrice")
    private int mAdDiscountPrice;
    @SerializedName("AdDiscountPercentage")
    private int AdDiscountPercentage;
    @SerializedName("OfferedDiscountPrice")
    private String mOfferedDiscountPrice;
    @SerializedName("OfferedDiscountPercentage")
    private String mOfferedDiscountPercentage;
    @SerializedName("IsActive")
    private String mIsActive;
    @SerializedName("PostedOn")
    private String mPostedOn;
    @SerializedName("PostedBy")
    private String mPostedBy;
    @SerializedName("UpdatedOn")
    private String mUpdatedOn;
    @SerializedName("UpdatedBy")
    private String mUpdatedBy;
    @SerializedName("DealPriority")
    private String mDealPriority;
    @SerializedName("CommissionPercentage")
    private String mCommissionPercentage;
    @SerializedName("DealId")
    private int mDealId;
    @SerializedName("DealPrice")
    private int mDealPrice;
    @SerializedName("DealTitle")
    private String mDealTitle;
    @SerializedName("AccountsTitle")
    private String mAccountsTitle;
    @SerializedName("ImageLink")
    private String mImageLink;
    @SerializedName("RegularPrice")
    private int mRegularPrice;
    @SerializedName("EventPrice")
    private int mEventPrice;
    @SerializedName("OfferTag")
    private String mOfferTag;

    public EventPayload(int mId, int mEventId, int mCouponPrice, int mTotalDiscountPercentage, int mAdDiscountPrice, int adDiscountPercentage, String mOfferedDiscountPrice, String mOfferedDiscountPercentage, String mIsActive, String mPostedOn, String mPostedBy, String mUpdatedOn, String mUpdatedBy, String mDealPriority, String mCommissionPercentage, int mDealId, int mDealPrice, String mDealTitle, String mAccountsTitle, String mImageLink, int mRegularPrice, int mEventPrice, String mOfferTag) {
        this.mId = mId;
        this.mEventId = mEventId;
        this.mCouponPrice = mCouponPrice;
        this.mTotalDiscountPercentage = mTotalDiscountPercentage;
        this.mAdDiscountPrice = mAdDiscountPrice;
        AdDiscountPercentage = adDiscountPercentage;
        this.mOfferedDiscountPrice = mOfferedDiscountPrice;
        this.mOfferedDiscountPercentage = mOfferedDiscountPercentage;
        this.mIsActive = mIsActive;
        this.mPostedOn = mPostedOn;
        this.mPostedBy = mPostedBy;
        this.mUpdatedOn = mUpdatedOn;
        this.mUpdatedBy = mUpdatedBy;
        this.mDealPriority = mDealPriority;
        this.mCommissionPercentage = mCommissionPercentage;
        this.mDealId = mDealId;
        this.mDealPrice = mDealPrice;
        this.mDealTitle = mDealTitle;
        this.mAccountsTitle = mAccountsTitle;
        this.mImageLink = mImageLink;
        this.mRegularPrice = mRegularPrice;
        this.mEventPrice = mEventPrice;
        this.mOfferTag = mOfferTag;
    }

    public int getmId() {
        return mId;
    }

    public int getmEventId() {
        return mEventId;
    }

    public int getmCouponPrice() {
        return mCouponPrice;
    }

    public int getmTotalDiscountPercentage() {
        return mTotalDiscountPercentage;
    }

    public int getmAdDiscountPrice() {
        return mAdDiscountPrice;
    }

    public int getAdDiscountPercentage() {
        return AdDiscountPercentage;
    }

    public String getmOfferedDiscountPrice() {
        return mOfferedDiscountPrice;
    }

    public String getmOfferedDiscountPercentage() {
        return mOfferedDiscountPercentage;
    }

    public String getmIsActive() {
        return mIsActive;
    }

    public String getmPostedOn() {
        return mPostedOn;
    }

    public String getmPostedBy() {
        return mPostedBy;
    }

    public String getmUpdatedOn() {
        return mUpdatedOn;
    }

    public String getmUpdatedBy() {
        return mUpdatedBy;
    }

    public String getmDealPriority() {
        return mDealPriority;
    }

    public String getmCommissionPercentage() {
        return mCommissionPercentage;
    }

    public int getmDealId() {
        return mDealId;
    }

    public int getmDealPrice() {
        return mDealPrice;
    }

    public String getmDealTitle() {
        return mDealTitle;
    }

    public String getmAccountsTitle() {
        return mAccountsTitle;
    }

    public String getmImageLink() {
        return mImageLink;
    }

    public int getmRegularPrice() {
        return mRegularPrice;
    }

    public int getmEventPrice() {
        return mEventPrice;
    }

    public String getmOfferTag() {
        return mOfferTag;
    }


    @Override
    public String toString() {
        return "EventPayload{" +
                "mId=" + mId +
                ", mEventId=" + mEventId +
                ", mCouponPrice=" + mCouponPrice +
                ", mTotalDiscountPercentage=" + mTotalDiscountPercentage +
                ", mAdDiscountPrice=" + mAdDiscountPrice +
                ", AdDiscountPercentage=" + AdDiscountPercentage +
                ", mOfferedDiscountPrice='" + mOfferedDiscountPrice + '\'' +
                ", mOfferedDiscountPercentage='" + mOfferedDiscountPercentage + '\'' +
                ", mIsActive='" + mIsActive + '\'' +
                ", mPostedOn='" + mPostedOn + '\'' +
                ", mPostedBy='" + mPostedBy + '\'' +
                ", mUpdatedOn='" + mUpdatedOn + '\'' +
                ", mUpdatedBy='" + mUpdatedBy + '\'' +
                ", mDealPriority='" + mDealPriority + '\'' +
                ", mCommissionPercentage='" + mCommissionPercentage + '\'' +
                ", mDealId=" + mDealId +
                ", mDealPrice=" + mDealPrice +
                ", mDealTitle='" + mDealTitle + '\'' +
                ", mAccountsTitle='" + mAccountsTitle + '\'' +
                ", mImageLink='" + mImageLink + '\'' +
                ", mRegularPrice=" + mRegularPrice +
                ", mEventPrice=" + mEventPrice +
                ", mOfferTag='" + mOfferTag + '\'' +
                '}';
    }
}
