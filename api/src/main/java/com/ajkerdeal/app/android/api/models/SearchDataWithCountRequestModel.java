package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/20/16.
 */
public class SearchDataWithCountRequestModel {
    @SerializedName("Keywords")
    private String Keywords;

    @SerializedName("SearchIn")
    private int SearchIn;

    @SerializedName("Index")
    private int Index;

    @SerializedName("Count")
    private int Count;


    public SearchDataWithCountRequestModel(String keywords, int searchIn, int index, int count) {
        Keywords = keywords;
        SearchIn = searchIn;
        Index = index;
        Count = count;
    }

    public String getKeywords() {
        return Keywords;
    }

    public int getSearchIn() {
        return SearchIn;
    }

    public int getIndex() {
        return Index;
    }

    public int getCount() {
        return Count;
    }

    @Override
    public String toString() {
        return "SearchDataWithCountRequestModel{" +
                "Keywords='" + Keywords + '\'' +
                ", SearchIn='" + SearchIn + '\'' +
                ", Index=" + Index +
                ", Count=" + Count +
                '}';
    }
}
