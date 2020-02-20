
package com.cloudwell.paywell.services.activity.modelPojo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserSubBusinessTypeModel {

    @SerializedName("channel")
    private String mChannel;
    @SerializedName("deviceId")
    private String mDeviceId;
    @SerializedName("format")
    private String mFormat;
    @SerializedName("ref_id")
    private String mRefId;
    @SerializedName("serviceId")
    private String mServiceId;
    @SerializedName("timestamp")
    private String mTimestamp;
    @SerializedName("username")
    private String mUsername;

    public String getChannel() {
        return mChannel;
    }

    public void setChannel(String channel) {
        mChannel = channel;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    public String getRefId() {
        return mRefId;
    }

    public void setRefId(String refId) {
        mRefId = refId;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String serviceId) {
        mServiceId = serviceId;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
