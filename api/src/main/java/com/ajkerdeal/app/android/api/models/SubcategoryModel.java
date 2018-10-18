package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by haideralikazal on 5/11/16.
 */
public class SubcategoryModel {
    @SerializedName("CategoryId")
    private int categoryId;
    @SerializedName("SubCategoryId")
    private int subCategoryId;
    @SerializedName("SubCategoryName")
    private String subCategoryName;
    @SerializedName("SubCategoryNameInEnglish")
    private String subCategoryNameInEnglish;
    @SerializedName("SubCategoryShowOrder")
    private int subCategoryShowOrder;

    public SubcategoryModel(int categoryId, int subCategoryId,
                            String subCategoryName, String subCategoryNameInEnglish,
                            int subCategoryShowOrder) {
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.subCategoryNameInEnglish = subCategoryNameInEnglish;
        this.subCategoryShowOrder = subCategoryShowOrder;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public String getSubCategoryNameInEnglish() {
        return subCategoryNameInEnglish;
    }

    public int getSubCategoryShowOrder() {
        return subCategoryShowOrder;
    }

    @Override
    public String toString() {
        return "SubcategoryModel {" +
                "categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", subCategoryNameInEnglish='" + subCategoryNameInEnglish + '\'' +
                ", subCategoryShowOrder=" + subCategoryShowOrder +
                '}';
    }
}
