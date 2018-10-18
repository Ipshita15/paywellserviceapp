package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rasel on 5/10/2016.
 */
public class CategoryModel {
    @SerializedName("CategoryId")
    private int categoryId;
    @SerializedName("CategoryName")
    private String categoryName;
    @SerializedName("CategoryNameInEnglish")
    private String categoryNameInEnglish;
    @SerializedName("CategoryShowOrder")
    private int categoryShowOrder;

    public CategoryModel(int categoryId, String categoryName, String categoryNameInEnglish, int categoryShowOrder) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryNameInEnglish = categoryNameInEnglish;
        this.categoryShowOrder = categoryShowOrder;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryNameInEnglish() {
        return categoryNameInEnglish;
    }

    public int getCategoryShowOrder() {
        return categoryShowOrder;
    }

    @Override
    public String toString() {
        return "CategoryModel {" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryNameInEnglish='" + categoryNameInEnglish + '\'' +
                ", categoryShowOrder=" + categoryShowOrder +
                '}';
    }
}
