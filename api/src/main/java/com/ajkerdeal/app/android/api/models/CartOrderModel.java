package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 4/16/17.
 */

public class CartOrderModel {
    @SerializedName("CouponId")
    private int mCouponId;
    @SerializedName("ShopCartId")
    private int mShoppingCartId;

    public CartOrderModel() {
    }

    public CartOrderModel(int mCouponId, int mShoppingCartId) {
        this.mCouponId = mCouponId;
        this.mShoppingCartId = mShoppingCartId;
    }

    public int getmCouponId() {
        return mCouponId;
    }

    public void setmCouponId(int mCouponId) {
        this.mCouponId = mCouponId;
    }

    public int getmShoppingCartId() {
        return mShoppingCartId;
    }

    public void setmShoppingCartId(int mShoppingCartId) {
        this.mShoppingCartId = mShoppingCartId;
    }

    @Override
    public String toString() {
        return "CartOrderModel{" +
                "mCouponId=" + mCouponId +
                ", mShoppingCartId=" + mShoppingCartId +
                '}';
    }

}
