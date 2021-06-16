package com.cloudwell.paywell.services.activity.refill.model;

import com.google.gson.annotations.SerializedName;

public class RequestRefillBalance {

    @SerializedName("username")
    private String mUsername;
    @SerializedName("bankId")
    private String mBankId;
    @SerializedName("districtId")
    private String mDistrictId;
    @SerializedName("branchId")
    private String mBranchId;
    @SerializedName("depositslip")
    private String mImagePath;

    @SerializedName("amount")
    private String amount;

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmBankId() {
        return mBankId;
    }

    public void setmBankId(String mBankId) {
        this.mBankId = mBankId;
    }

    public String getmDistrictId() {
        return mDistrictId;
    }

    public void setmDistrictId(String mDistrictId) {
        this.mDistrictId = mDistrictId;
    }

    public String getmBranchId() {
        return mBranchId;
    }

    public void setmBranchId(String mBranchId) {
        this.mBranchId = mBranchId;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public void setAmount(String toString) {
        this.amount = toString;

    }

    public void setBankId(String bankId) {
        mBankId = bankId;
    }

    public String getAmount() {
        return amount;
    }
}
