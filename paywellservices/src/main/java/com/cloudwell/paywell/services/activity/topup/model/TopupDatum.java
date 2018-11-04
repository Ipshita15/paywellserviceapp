
package com.cloudwell.paywell.services.activity.topup.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TopupDatum {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("topupData")
    private TopupData mTopupData;
    @SerializedName("trans_id")
    private String mTransId;

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

    public TopupData getTopupData() {
        return mTopupData;
    }

    public void setTopupData(TopupData topupData) {
        mTopupData = topupData;
    }

    public String getTransId() {
        return mTransId;
    }

    public void setTransId(String transId) {
        mTransId = transId;
    }

}
