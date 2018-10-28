
package com.cloudwell.paywell.services.activity.topup.brilliantFlow.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class APIBrilliantTRXLog {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("pwContact")
    private String mPwContact;
    @SerializedName("status_code")
    private Long mStatusCode;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
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
