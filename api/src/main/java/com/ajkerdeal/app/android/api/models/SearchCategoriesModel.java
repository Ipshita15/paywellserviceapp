package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 4/10/16.
 */
public class SearchCategoriesModel {
    @SerializedName("CategoryId")
    private int CategoryId;
    @SerializedName("HasProduct")
    private int HasProduct;
    @SerializedName("Category")
    private String Category;

    private String mEmpty1;
    private String mEmpty2;
    private String mEmpty3;


    public SearchCategoriesModel(int categoryId, int hasProduct, String category) {
        CategoryId = categoryId;
        HasProduct = hasProduct;
        Category = category;
    }

    public SearchCategoriesModel(String s, String s1, String s3) {
        mEmpty1 = s;
        mEmpty2 = s1;
        mEmpty3 = s3;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getHasProduct() {
        return HasProduct;
    }

    public String getCategory() {
        return Category;
    }

    @Override
    public String toString() {
        return "SearchCategoriesModel{" +
                "CategoryId=" + CategoryId +
                ", HasProduct=" + HasProduct +
                ", Category='" + Category + '\'' +
                ", mEmpty1='" + mEmpty1 + '\'' +
                ", mEmpty2='" + mEmpty2 + '\'' +
                ", mEmpty3='" + mEmpty3 + '\'' +
                '}';
    }
}
