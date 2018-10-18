package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

public class HomeCategoryBlockProductModel {

    @SerializedName("CategoryId")
    private int categoryId;

    @SerializedName("SubcategoryId")
    private int subcategoryId;

    @SerializedName("SubSubcategoryId")
    private int subSubcategoryId;

    @SerializedName("DealId")
    private int dealId;

    @SerializedName("DealTitle")
    private String dealTitle;

    @SerializedName("DealPrice")
    private int dealPrice;

    @SerializedName("FolderName")
    private String folderName;
    private int ImageLocation;

    public HomeCategoryBlockProductModel(int categoryId, int subcategoryId, int subSubcategoryId, int dealId, String dealTitle, int dealPrice, String folderName) {
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.subSubcategoryId = subSubcategoryId;
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;
        this.folderName = folderName;
    }

    public int getImageLocation() {
        return ImageLocation;
    }

    public void setImageLocation(int imageLocation) {
        ImageLocation = imageLocation;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public int getSubSubcategoryId() {
        return subSubcategoryId;
    }

    public int getDealId() {
        return dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public int getDealPrice() {
        return dealPrice;
    }

    public String getFolderName() {
        return folderName;
    }

}