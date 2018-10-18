package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 5/21/16.
 */
public class CategorywisePriceRangeModel {

           int RangeID;
           int CategoryId;
           int SubCategoryId;
           int MinRange;
           int MaxRange;
           int OrderBy;
           Boolean IsActive;
           String  MinRangeBng;
           String  MaxRangeBng;

    public CategorywisePriceRangeModel(int rangeID, int categoryId, int subCategoryId, int minRange, int maxRange, int orderBy, Boolean isActive, String minRangeBng, String maxRangeBng) {
        RangeID = rangeID;
        CategoryId = categoryId;
        SubCategoryId = subCategoryId;
        MinRange = minRange;
        MaxRange = maxRange;
        OrderBy = orderBy;
        IsActive = isActive;
        MinRangeBng = minRangeBng;
        MaxRangeBng = maxRangeBng;
    }

    public int getRangeID() {
        return RangeID;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public int getMinRange() {
        return MinRange;
    }

    public int getMaxRange() {
        return MaxRange;
    }

    public int getOrderBy() {
        return OrderBy;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public String getMinRangeBng() {
        return MinRangeBng;
    }

    public String getMaxRangeBng() {
        return MaxRangeBng;
    }
}
