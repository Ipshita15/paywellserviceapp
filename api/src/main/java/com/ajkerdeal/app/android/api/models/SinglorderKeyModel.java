package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 5/31/16.
 */
public class SinglorderKeyModel {
    @SerializedName("CouponId")
    private int CouponId;

    public SinglorderKeyModel(int couponId) {
        CouponId = couponId;
    }

    public int getCouponId() {
        return CouponId;
    }

    @Override
    public String toString() {
        return "SinglorderKeyModel{" +
                "CouponId=" + CouponId +
                '}';
    }
}
