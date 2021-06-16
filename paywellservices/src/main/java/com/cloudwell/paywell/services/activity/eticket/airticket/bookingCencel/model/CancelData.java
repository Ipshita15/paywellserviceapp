package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model;

import com.google.gson.annotations.SerializedName;

public class CancelData {

    @SerializedName("ReIssue")
    private String mReIssue;
    @SerializedName("InfoUpdate")
    private String InfoUpdate;
    @SerializedName("Refund")
    private String mRefund;
    @SerializedName("Void")
    private String mVoid;

    public String getReIssue() {
        return mReIssue;
    }


    public String getInfoUpdate() {
        return InfoUpdate;
    }

    public String getRefund() {
        return mRefund;
    }

    public void setRefund(String refund) {
        mRefund = refund;
    }

    public String getVoid() {
        return mVoid;
    }

    public void setVoid(String aVoid) {
        mVoid = aVoid;
    }

}
