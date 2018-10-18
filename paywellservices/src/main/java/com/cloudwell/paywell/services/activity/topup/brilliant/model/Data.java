
package com.cloudwell.paywell.services.activity.topup.brilliant.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("add_datetime")
    private String mAddDatetime;
    @SerializedName("amount")
    private String mAmount;
    @SerializedName("briliant_trx_id")
    private String mBriliantTrxId;
    @SerializedName("brilliant_mobile_number")
    private String mBrilliantMobileNumber;
    @SerializedName("paywell_trx_id")
    private String mPaywellTrxId;
    @SerializedName("status_name")
    private String mStatusName;

    public String getAddDatetime() {
        return mAddDatetime;
    }

    public void setAddDatetime(String addDatetime) {
        mAddDatetime = addDatetime;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getBriliantTrxId() {
        return mBriliantTrxId;
    }

    public void setBriliantTrxId(String briliantTrxId) {
        mBriliantTrxId = briliantTrxId;
    }

    public String getBrilliantMobileNumber() {
        return mBrilliantMobileNumber;
    }

    public void setBrilliantMobileNumber(String brilliantMobileNumber) {
        mBrilliantMobileNumber = brilliantMobileNumber;
    }

    public String getPaywellTrxId() {
        return mPaywellTrxId;
    }

    public void setPaywellTrxId(String paywellTrxId) {
        mPaywellTrxId = paywellTrxId;
    }

    public String getStatusName() {
        return mStatusName;
    }

    public void setStatusName(String statusName) {
        mStatusName = statusName;
    }

}
