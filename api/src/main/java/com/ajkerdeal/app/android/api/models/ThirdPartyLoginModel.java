package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamim on 8/1/2016.
 */
public class ThirdPartyLoginModel {

    @SerializedName("Name")
    private String thirdPartyName;

    @SerializedName("Email")
    private String thirdPartyEmail;

    public ThirdPartyLoginModel(String thirdPartyName, String thirdPartyEmail) {
        this.thirdPartyName = thirdPartyName;
        this.thirdPartyEmail = thirdPartyEmail;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyEmail() {
        return thirdPartyEmail;
    }

    public void setThirdPartyEmail(String thirdPartyEmail) {
        this.thirdPartyEmail = thirdPartyEmail;
    }
}
