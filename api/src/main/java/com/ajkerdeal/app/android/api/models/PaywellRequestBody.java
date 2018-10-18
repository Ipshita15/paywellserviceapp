package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sumaya on 10-Oct-17.
 */

public class PaywellRequestBody {

    @SerializedName("ShopCartId")
    private int mShopCartID;
    @SerializedName("CouponId")
    private int CouponID;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("Comments")
    private String mComments;
    @SerializedName("CommentedBy")
    private int mCommentedBy;

    public PaywellRequestBody() {
    }

    public PaywellRequestBody(int mShopCartID, int couponID, String mStatus, String mComments, int mCommentedBy) {
        this.mShopCartID = mShopCartID;
        CouponID = couponID;
        this.mStatus = mStatus;
        this.mComments = mComments;
        this.mCommentedBy = mCommentedBy;
    }

    public int getmShopCartID() {
        return mShopCartID;
    }

    public int getCouponID() {
        return CouponID;
    }

    public String getmStatus() {
        return mStatus;
    }

    public String getmComments() {
        return mComments;
    }

    public int getmCommentedBy() {
        return mCommentedBy;
    }

    @Override
    public String toString() {
        return "PaywellRequestBody{" +
                "mShopCartID=" + mShopCartID +
                ", CouponID=" + CouponID +
                ", mStatus='" + mStatus + '\'' +
                ", mComments='" + mComments + '\'' +
                ", mCommentedBy=" + mCommentedBy +
                '}';
    }
}
