package com.ajkerdeal.app.android.api.models;

/**
 * Created by Rasel on 1/3/2017.
 */

public class BannerFilterRequestBody {
    int CategoryId;
    int SubCategoryId;
    int SubSubCategoryId;
    int MerchantId;
    int DealId;
    int Index;
    int Count;

    public BannerFilterRequestBody() {
    }

    public BannerFilterRequestBody(int categoryId, int subCategoryId, int subSubCategoryId, int merchantId, int dealId, int index, int count) {
        CategoryId = categoryId;
        SubCategoryId = subCategoryId;
        SubSubCategoryId = subSubCategoryId;
        MerchantId = merchantId;
        DealId = dealId;
        Index = index;
        Count = count;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public int getSubSubCategoryId() {
        return SubSubCategoryId;
    }

    public void setSubSubCategoryId(int subSubCategoryId) {
        SubSubCategoryId = subSubCategoryId;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public int getDealId() {
        return DealId;
    }

    public void setDealId(int dealId) {
        DealId = dealId;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public String toString() {
        return "BannerFilterRequestBody{" +
                "CategoryId=" + CategoryId +
                ", SubCategoryId=" + SubCategoryId +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", MerchantId=" + MerchantId +
                ", DealId=" + DealId +
                ", Index=" + Index +
                ", Count=" + Count +
                '}';
    }
}
