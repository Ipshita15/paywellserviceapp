package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/10/17.
 */

public class SearchProductRequestBody {

    @SerializedName("Keyword")
    public String keyword;

    @SerializedName("CategoryId")
    public int categoryId;

    @SerializedName("SubCategoryId")
    public int subCategoryId;

    @SerializedName("SubSubCategoryId")
    public int subSubCategoryId;

    @SerializedName("MerchantId")
    public int merchantId;

    @SerializedName("BrandId")
    public int brandId;

    public SearchProductRequestBody() {
    }

    public SearchProductRequestBody(String keyword, int categoryId, int subCategoryId, int subSubCategoryId, int merchantId, int brandId) {
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subSubCategoryId = subSubCategoryId;
        this.merchantId = merchantId;
        this.brandId = brandId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getSubSubCategoryId() {
        return subSubCategoryId;
    }

    public void setSubSubCategoryId(int subSubCategoryId) {
        this.subSubCategoryId = subSubCategoryId;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "SearchProductRequestBody{" +
                "keyword='" + keyword + '\'' +
                ", categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                ", subSubCategoryId=" + subSubCategoryId +
                ", merchantId=" + merchantId +
                ", brandId=" + brandId +
                '}';
    }
}
