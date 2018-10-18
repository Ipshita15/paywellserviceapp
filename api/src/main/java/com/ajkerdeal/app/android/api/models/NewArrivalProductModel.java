package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pony on 1/25/17.
 */

public class NewArrivalProductModel {

    @SerializedName("DealId")

    public int dealId;
    @SerializedName("DealTitle")

    public String dealTitle;
    @SerializedName("CategoryId")

    public int categoryId;
    @SerializedName("SubCategoryId")

    public int subCategoryId;
    @SerializedName("FolderName")

    public String folderName;
    @SerializedName("ImageLink")

    public String imageLink;
    @SerializedName("IsSoldOut")

    public int isSoldOut;
    @SerializedName("DealPriority")

    public int dealPriority;
    @SerializedName("AccountsTitle")

    public String accountsTitle;
    @SerializedName("CustomiseMsg")

    public String customiseMsg;
    @SerializedName("DealPrice")

    public int dealPrice;
    @SerializedName("Subcategory")

    public String subcategory;
    @SerializedName("Category")
    public String category;
    @SerializedName("SubcategoryEng")
    public String subcategoryEng;

    @SerializedName("CategoryEng")
    public String categoryEng;

    public NewArrivalProductModel() {
    }

    public NewArrivalProductModel(int dealId, String dealTitle, int categoryId, int subCategoryId, String folderName, String imageLink, int isSoldOut, int dealPriority, String accountsTitle, String customiseMsg, int dealPrice, String subcategory, String category, String subcategoryEng, String categoryEng) {
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.folderName = folderName;
        this.imageLink = imageLink;
        this.isSoldOut = isSoldOut;
        this.dealPriority = dealPriority;
        this.accountsTitle = accountsTitle;
        this.customiseMsg = customiseMsg;
        this.dealPrice = dealPrice;
        this.subcategory = subcategory;
        this.category = category;
        this.subcategoryEng = subcategoryEng;
        this.categoryEng = categoryEng;
    }

    public int getDealId() {
        return dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public int getIsSoldOut() {
        return isSoldOut;
    }

    public int getDealPriority() {
        return dealPriority;
    }

    public String getAccountsTitle() {
        return accountsTitle;
    }

    public String getCustomiseMsg() {
        return customiseMsg;
    }

    public int getDealPrice() {
        return dealPrice;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategoryEng() {
        return subcategoryEng;
    }

    public String getCategoryEng() {
        return categoryEng;
    }

    @Override
    public String toString() {
        return "NewArrivalProductModel{" +
                "dealId=" + dealId +
                ", dealTitle='" + dealTitle + '\'' +
                ", categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                ", folderName='" + folderName + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", isSoldOut=" + isSoldOut +
                ", dealPriority=" + dealPriority +
                ", accountsTitle='" + accountsTitle + '\'' +
                ", customiseMsg='" + customiseMsg + '\'' +
                ", dealPrice=" + dealPrice +
                ", subcategory='" + subcategory + '\'' +
                ", category='" + category + '\'' +
                ", subcategoryEng='" + subcategoryEng + '\'' +
                ", categoryEng='" + categoryEng + '\'' +
                '}';
    }
}

