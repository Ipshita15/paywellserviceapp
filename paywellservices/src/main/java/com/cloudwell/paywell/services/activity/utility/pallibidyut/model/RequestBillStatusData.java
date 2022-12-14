
package com.cloudwell.paywell.services.activity.utility.pallibidyut.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RequestBillStatusData {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("sms")
    private String mSms;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("trx_id")
    private String mTrxId;

    @SerializedName("ApiStatusName")
    private String ApiStatusName;

    @SerializedName("ApiStatus")
    private int ApiStatus;




    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getSms() {
        return mSms;
    }

    public void setSms(String sms) {
        mSms = sms;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public String getTrxId() {
        return mTrxId;
    }

    public void setTrxId(String trxId) {
        mTrxId = trxId;
    }


    public int getApiStatus() {
        return ApiStatus;
    }

    public void setApiStatus(int apiStatus) {
        ApiStatus = apiStatus;
    }

    public void setApiStatusName(String apiStatusName) {
        ApiStatusName = apiStatusName;
    }

    public String getApiStatusName() {
        return ApiStatusName;
    }
}
