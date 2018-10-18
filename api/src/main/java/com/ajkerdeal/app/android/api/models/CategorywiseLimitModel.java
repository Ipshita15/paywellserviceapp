package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 4/13/16.
 */
public class CategorywiseLimitModel {

    private String BrandId;
    private String MerchantId;
    private String MinPrice;
    private String MaxPrice;
    private String  Index;
    private String   Count;
    private String TopSellDealSet;
    private String OrderBy;
    private String SortType;

    public CategorywiseLimitModel(String index, String  count) {
        Index = index;
        Count = count;
    }

    public CategorywiseLimitModel(String brandId, String merchantId, String minPrice, String maxPrice, String index, String count, String topSellDealSet, String orderBy, String sortType) {
        BrandId = brandId;
        MerchantId = merchantId;
        MinPrice = minPrice;
        MaxPrice = maxPrice;
        Index = index;
        Count = count;
        TopSellDealSet = topSellDealSet;
        OrderBy = orderBy;
        SortType = sortType;
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

    public String getIndex() {
        return Index;
    }

    public String getCount() {
        return Count;
    }

    public String getTopSellDealSet() {
        return TopSellDealSet;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public String getSortType() {
        return SortType;
    }

    @Override
    public String toString() {
        return "CategorywiseLimitModel{" +
                "BrandId='" + BrandId + '\'' +
                ", MerchantId='" + MerchantId + '\'' +
                ", MinPrice='" + MinPrice + '\'' +
                ", MaxPrice='" + MaxPrice + '\'' +
                ", Index='" + Index + '\'' +
                ", Count='" + Count + '\'' +
                ", TopSellDealSet='" + TopSellDealSet + '\'' +
                ", OrderBy='" + OrderBy + '\'' +
                ", SortType='" + SortType + '\'' +
                '}';
    }
}
