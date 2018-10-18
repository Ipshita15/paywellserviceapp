package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/20/16.
 */
public class SearchCountRequestModel {
    @SerializedName("Keywords")
    private String Keywords;

    @SerializedName("SearchIn")
    private int SearchIn;

    public SearchCountRequestModel(String keywords, int searchIn) {
        Keywords = keywords;
        SearchIn = searchIn;
    }

    public String getKeywords() {
        return Keywords;
    }

    public int getSearchIn() {
        return SearchIn;
    }

    @Override
    public String toString() {
        return "SearchCountRequestModel{" +
                "Keywords='" + Keywords + '\'' +
                ", SearchIn=" + SearchIn +
                '}';
    }
}
