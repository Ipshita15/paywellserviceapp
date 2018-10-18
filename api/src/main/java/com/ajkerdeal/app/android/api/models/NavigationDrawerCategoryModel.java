package com.ajkerdeal.app.android.api.models;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Kazal on 06-Mar-16.
 */

public class NavigationDrawerCategoryModel {
    @SerializedName("CategoryId")
    private int categoryId;
    @SerializedName("Category")
    private String category;
    @SerializedName("LogoLink")
    private String logoLink;
    @SerializedName("ColorCode")
    private String colorCode;

    public NavigationDrawerCategoryModel(int categoryId, String category, String logoLink,
                                         String colorCode) {
        this.categoryId = categoryId;
        this.category = category;
        this.logoLink = logoLink;
        this.colorCode = colorCode;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public String getColorCode() {
        return colorCode;
    }

    @Override
    public String toString() {
        return "NavigationDrawerCategoryModel {" +
                "CategoryId=" + categoryId +
                ", category='" + category + '\'' +
                ", logoLink='" + logoLink + '\'' +
                ", colorCode='" + colorCode + '\'' +
                '}';
    }
}
