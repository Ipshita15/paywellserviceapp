package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by haideralikazal on 5/11/16.
 */
public class SubSubcategoryModel {
    @SerializedName("CategoryId")
    private int categoryId;
    @SerializedName("SubCategoryId")
    private int subCategoryId;
    @SerializedName("SubSubCategoryId")
    private int subSubCategoryId;
    @SerializedName("SubSubCategoryName")
    private String subSubCategoryName;
    @SerializedName("SubSubCategoryNameInEnglish")
    private String subSubCategoryNameInEnglish;
    @SerializedName("SubSubCategoryShowOrder")
    private int subSubCategoryShowOrder;

    public SubSubcategoryModel(int categoryId, int subCategoryId, int subSubCategoryId,
                               String subSubCategoryName, String subSubCategoryNameInEnglish,
                               int subSubCategoryShowOrder) {
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subSubCategoryId = subSubCategoryId;
        this.subSubCategoryName = subSubCategoryName;
        this.subSubCategoryNameInEnglish = subSubCategoryNameInEnglish;
        this.subSubCategoryShowOrder = subSubCategoryShowOrder;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public int getSubSubCategoryId() {
        return subSubCategoryId;
    }

    public String getSubSubCategoryName() {
        return subSubCategoryName;
    }

    public String getSubSubCategoryNameInEnglish() {
        return subSubCategoryNameInEnglish;
    }

    public int getSubSubCategoryShowOrder() {
        return subSubCategoryShowOrder;
    }

    @Override
    public String toString() {
        return "SubSubcategoryModel {" +
                "categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                ", subSubCategoryId=" + subSubCategoryId +
                ", subSubCategoryName='" + subSubCategoryName + '\'' +
                ", subSubCategoryNameInEnglish='" + subSubCategoryNameInEnglish + '\'' +
                ", subSubCategoryShowOrder=" + subSubCategoryShowOrder +
                '}';
    }
}
