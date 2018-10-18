package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 8/21/16.
 */
public class SearchByCategoryWiseDealsModel {
    @SerializedName("SearchData")
    private String SearchData;

    @SerializedName("CategoryId")
    private int CategoryId;

    @SerializedName("Index")
    private int Index;

    @SerializedName("Count")
    private int Count;

    public SearchByCategoryWiseDealsModel(String searchData, int categoryId, int index, int count) {
        SearchData = searchData;
        CategoryId = categoryId;
        Index = index;
        Count = count;
    }

    public String getSearchData() {
        return SearchData;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getIndex() {
        return Index;
    }

    public int getCount() {
        return Count;
    }

    @Override
    public String toString() {
        return "SearchByCategoryWiseDealsModel{" +
                "SearchData='" + SearchData + '\'' +
                ", CategoryId=" + CategoryId +
                ", Index=" + Index +
                ", Count=" + Count +
                '}';
    }
}
