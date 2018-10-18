package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/10/17.
 */

public class CategorySuggestion {

    @SerializedName("Id")

    public String id;
    @SerializedName("CategoryId")

    public int categoryId;
    @SerializedName("SubCategoryId")

    public int subCategoryId;
    @SerializedName("SubSubCategoryId")

    public int subSubCategoryId;
    @SerializedName("CategoryName")

    public String categoryName;
    @SerializedName("CategoryNameEng")

    public String categoryNameEng;
    @SerializedName("RouteName")

    public String routeName;
    @SerializedName("ProductCount")

    public int productCount;

    public CategorySuggestion(String id, int categoryId, int subCategoryId, int subSubCategoryId, String categoryName, String categoryNameEng, String routeName, int productCount) {
        this.id = id;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subSubCategoryId = subSubCategoryId;
        this.categoryName = categoryName;
        this.categoryNameEng = categoryNameEng;
        this.routeName = routeName;
        this.productCount = productCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNameEng() {
        return categoryNameEng;
    }

    public void setCategoryNameEng(String categoryNameEng) {
        this.categoryNameEng = categoryNameEng;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
