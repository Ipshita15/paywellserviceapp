
package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model;


import com.google.gson.annotations.SerializedName;

public class DescoBillPaySubmitResponseDetails {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("msg_text")
    private String mMsgText;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("trans_id")
    private String mTransId;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMsgText() {
        return mMsgText;
    }

    public void setMsgText(String msgText) {
        mMsgText = msgText;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public String getTransId() {
        return mTransId;
    }

    public void setTransId(String transId) {
        mTransId = transId;
    }

}
