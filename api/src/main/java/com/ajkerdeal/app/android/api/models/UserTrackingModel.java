package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamim on 10/25/2016.
 */

public class UserTrackingModel {

    @SerializedName("AppId")
    private String appId;

    @SerializedName("CustomerId")
    private int customerId;

    @SerializedName("DeviceId")
    private String deviceId;

    public UserTrackingModel(String appId, int customerId, String deviceId) {
        this.appId = appId;
        this.customerId = customerId;
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getDeviceId() {
        return deviceId;
    }


}
