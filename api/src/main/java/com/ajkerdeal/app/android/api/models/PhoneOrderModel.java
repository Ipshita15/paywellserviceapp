package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 10/19/16.
 */

public class PhoneOrderModel {
    @SerializedName("pc_name")
    private String mDeviceName;

    @SerializedName("username")
    private String mUserName;

    @SerializedName("passwrd")
    private String mPassword;

    @SerializedName("url")
    private String mUrl;

    @SerializedName("log_type")
    private String mLogType;

    @SerializedName("log_from")
    private String mLogFrom;


    public PhoneOrderModel(String mDeviceName, String mUserName, String mPassword, String mUrl, String mLogType, String mLogFrom) {
        this.mDeviceName = mDeviceName;
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        this.mUrl = mUrl;
        this.mLogType = mLogType;
        this.mLogFrom = mLogFrom;
    }

    public String getmDeviceName() {
        return mDeviceName;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmLogType() {
        return mLogType;
    }

    public String getmLogFrom() {
        return mLogFrom;
    }
}
