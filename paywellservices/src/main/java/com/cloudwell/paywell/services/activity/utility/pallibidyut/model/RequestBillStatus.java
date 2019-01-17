package com.cloudwell.paywell.services.activity.utility.pallibidyut.model;

import com.google.gson.annotations.SerializedName;

public class RequestBillStatus {

    @SerializedName("username")
    private String mUsername;
    @SerializedName("pass")
    private String mPassword;
    @SerializedName("account_no")
    private String mAccountNo;
    @SerializedName("month")
    private String mMonth;
    @SerializedName("year")
    private String mYear;
    @SerializedName("format")
    private String mFormat;

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmAccountNo() {
        return mAccountNo;
    }

    public void setmAccountNo(String mAccountNo) {
        this.mAccountNo = mAccountNo;
    }

    public String getmMonth() {
        return mMonth;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmFormat() {
        return mFormat;
    }

    public void setmFormat(String mFormat) {
        this.mFormat = mFormat;
    }
}
