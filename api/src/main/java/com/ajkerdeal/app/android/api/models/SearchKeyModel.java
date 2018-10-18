package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 6/2/16.
 */
public class SearchKeyModel {
    @SerializedName("Keywords")
    private String Keywords;

    public SearchKeyModel(String keywords) {

        Keywords = keywords;
    }

    public String getKeywords() {
        return Keywords;
    }

    @Override
    public String toString() {
        return "SearchKeyModel{" +
                "Keywords='" + Keywords + '\'' +
                '}';
    }
}
