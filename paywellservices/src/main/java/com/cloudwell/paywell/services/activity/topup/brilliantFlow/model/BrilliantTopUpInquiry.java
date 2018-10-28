
package com.cloudwell.paywell.services.activity.topup.brilliantFlow.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BrilliantTopUpInquiry {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("pwContact")
    private String mPwContact;
    @SerializedName("status_code")
    private Long mStatusCode;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getPwContact() {
        return mPwContact;
    }

    public void setPwContact(String pwContact) {
        mPwContact = pwContact;
    }

    public Long getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(Long statusCode) {
        mStatusCode = statusCode;
    }

}
