package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamim on 12/29/2016.
 */

public class PromotionBannerReqModel {

    @SerializedName("MerchantId")
    private int merchantId;

    @SerializedName("CategoryId")
    private int categoryId;

    @SerializedName("DealId")
    private int dealId;

    @SerializedName("Index")
    private int index;

    @SerializedName("Count")
    private int count;

    public PromotionBannerReqModel(int merchantId, int categoryId, int dealId, int index, int count) {
        this.merchantId = merchantId;
        this.categoryId = categoryId;
        this.dealId = dealId;
        this.index = index;
        this.count = count;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getDealId() {
        return dealId;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }
}
