package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/21/16.
 */
public class SearchInMerchantRequestModel {
    @SerializedName("Keywords")
    private String Keywords;

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("Index")
    private int Index;

    @SerializedName("Count")
    private int Count;

    public SearchInMerchantRequestModel(String keywords, int profileId, int index, int count) {
        Keywords = keywords;
        ProfileId = profileId;
        Index = index;
        Count = count;
    }

    public String getKeywords() {
        return Keywords;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public int getIndex() {
        return Index;
    }

    public int getCount() {
        return Count;
    }

    @Override
    public String toString() {
        return "SearchInMerchantRequestModel{" +
                "Keywords='" + Keywords + '\'' +
                ", ProfileId=" + ProfileId +
                ", Index=" + Index +
                ", Count=" + Count +
                '}';
    }
}
