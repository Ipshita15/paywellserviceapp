package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 5/7/16.
 */
public class CategorywiseFilterLimitModel {

    private String BrandId;
    private String MerchantId;
    private String MinPrice;
    private String MaxPrice;
    private String DealSet;

    public CategorywiseFilterLimitModel(String brandId, String merchantId, String minPrice, String maxPrice, String dealSet) {
        BrandId = brandId;
        MerchantId = merchantId;
        MinPrice = minPrice;
        MaxPrice = maxPrice;
        DealSet = dealSet;
    }

    public String getBrandId() {
        return BrandId;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public String getMinPrice() {
        return MinPrice;
    }

    public String getMaxPrice() {
        return MaxPrice;
    }

    public String getDealSet() {
        return DealSet;
    }

    @Override
    public String toString() {
        return "CategorywiseFilterLimitModel{" +
                "BrandId='" + BrandId + '\'' +
                ", MerchantId='" + MerchantId + '\'' +
                ", MinPrice='" + MinPrice + '\'' +
                ", MaxPrice='" + MaxPrice + '\'' +
                ", DealSet='" + DealSet + '\'' +
                '}';
    }
}
