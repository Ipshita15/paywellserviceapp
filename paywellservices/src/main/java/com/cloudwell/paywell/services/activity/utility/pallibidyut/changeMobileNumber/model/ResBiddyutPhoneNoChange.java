
package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ResBiddyutPhoneNoChange {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("trx_id")
    private String mTrxId;
    @SerializedName("sms")
    private String sms;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
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

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
