package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arifhasnat on 12/24/15.
 */
public class HomeCategoryModel {
    @SerializedName("CategoryId")
    private int categoryId;
    @SerializedName("Category")
    private String category;
    @SerializedName("Subcategories")
    private String subcategories;
    @SerializedName("LogoLink")
    private String logoLink;

    public HomeCategoryModel(int categoryId, String category, String subcategories,
                             String logoLink) {
        this.categoryId = categoryId;
        this.category = category;
        this.subcategories = subcategories;
        this.logoLink = logoLink;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategories() {
        return subcategories;
    }

    public String getLogoLink() {
        return logoLink;
    }

    @Override
    public String toString() {
        return "HomeCategoryModel{" +
                "CategoryId=" + categoryId +
                ", Category='" + category + '\'' +
                ", Subcategories='" + subcategories + '\'' +
                ", LogoLink='" + logoLink + '\'' +
                '}';
    }
}
