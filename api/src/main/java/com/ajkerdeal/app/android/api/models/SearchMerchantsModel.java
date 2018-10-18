package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 4/10/16.
 */
public class SearchMerchantsModel {
    @SerializedName("ProfileId")
    private int ProfileId;
    @SerializedName("CompanyName")
    private String CompanyName;

    public SearchMerchantsModel(int profileId, String companyName) {
        ProfileId = profileId;
        CompanyName = companyName;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    @Override
    public String toString() {
        return "SearchMerchantsModel{" +
                "ProfileId=" + ProfileId +
                ", CompanyName='" + CompanyName + '\'' +
                '}';
    }
}
