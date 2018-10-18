package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 8/30/16.
 */
public class CwiseBodyModel {

    private int CategoryId;
    private int SubCategoryId;
    private int SubSubCategoryId;
    private int BrandId;
    private double MinPrice;
    private double MaxPrice;
    private String SortType;
    private String OrderBy;
    private int CustomerId;
    private String TopSellDealSet;
    private int MerchantId;
    private int Index;
    private int Count;

    public CwiseBodyModel(int categoryId, int subCategoryId, int subSubCategoryId, int brandId, double minPrice, double maxPrice, String sortType, String orderBy, int customerId, String topSellDealSet, int merchantId, int index, int count) {
        CategoryId = categoryId;
        SubCategoryId = subCategoryId;
        SubSubCategoryId = subSubCategoryId;
        BrandId = brandId;
        MinPrice = minPrice;
        MaxPrice = maxPrice;
        SortType = sortType;
        OrderBy = orderBy;
        CustomerId = customerId;
        TopSellDealSet = topSellDealSet;
        MerchantId = merchantId;
        Index = index;
        Count = count;
    }

    @Override
    public String toString() {
        return "CwiseBodyModel{" +
                "CategoryId=" + CategoryId +
                ", SubCategoryId=" + SubCategoryId +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", BrandId=" + BrandId +
                ", MinPrice=" + MinPrice +
                ", MaxPrice=" + MaxPrice +
                ", SortType='" + SortType + '\'' +
                ", OrderBy='" + OrderBy + '\'' +
                ", CustomerId=" + CustomerId +
                ", TopSellDealSet='" + TopSellDealSet + '\'' +
                ", MerchantId=" + MerchantId +
                ", LowerLimit=" + Index +
                ", UpperLimit=" + Count +
                '}';
    }
}
