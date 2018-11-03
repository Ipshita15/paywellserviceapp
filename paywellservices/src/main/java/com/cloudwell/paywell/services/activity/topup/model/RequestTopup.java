
package com.cloudwell.paywell.services.activity.topup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestTopup {

    @SerializedName("password")
    private String mPassword;
    @SerializedName("topupData")
    private List<TopupDatum> mTopupData;
    @SerializedName("userName")
    private String mUserName;

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public List<TopupDatum> getTopupData() {
        return mTopupData;
    }

    public void setTopupData(List<TopupDatum> topupData) {
        mTopupData = topupData;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }


}
