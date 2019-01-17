
package com.cloudwell.paywell.services.app.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class APIResBalanceCheck {

    @SerializedName("balanceData")
    private BalanceData mBalanceData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("retailerCode")
    private String mRetailerCode;
    @SerializedName("status")
    private Long mStatus;

    public BalanceData getBalanceData() {
        return mBalanceData;
    }

    public void setBalanceData(BalanceData balanceData) {
        mBalanceData = balanceData;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getRetailerCode() {
        return mRetailerCode;
    }

    public void setRetailerCode(String retailerCode) {
        mRetailerCode = retailerCode;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
