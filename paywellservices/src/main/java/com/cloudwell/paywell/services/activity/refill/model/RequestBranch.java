package com.cloudwell.paywell.services.activity.refill.model;

import com.google.gson.annotations.SerializedName;

public class RequestBranch {
    @SerializedName("username")
    private String mUsername;
    @SerializedName("bankId")
    private String mBankId;
    @SerializedName("districtId")
    private String mDistrictId;

    public String getmUsername() {
        return mUsername;
    }

    public String getmBankId() {
        return mBankId;
    }

    public String getmDistrictId() {
        return mDistrictId;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmBankId(String mBankId) {
        this.mBankId = mBankId;
    }

    public void setmDistrictId(String mDistrictId) {
        this.mDistrictId = mDistrictId;
    }
}
