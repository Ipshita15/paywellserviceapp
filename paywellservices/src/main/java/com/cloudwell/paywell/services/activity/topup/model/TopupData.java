
package com.cloudwell.paywell.services.activity.topup.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class TopupData {

    @SerializedName("amount")
    private String mAmount;
    @SerializedName("con_type")
    private String mConType;
    @SerializedName("msisdn")
    private String mMsisdn;
    @SerializedName("operator")
    private String mOperator;

    public TopupData(String amount, String conType, String msisdn, String operator) {
        mAmount = amount;
        mConType = conType;
        mMsisdn = msisdn;
        mOperator = operator;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getConType() {
        return mConType;
    }

    public void setConType(String conType) {
        mConType = conType;
    }

    public String getMsisdn() {
        return mMsisdn;
    }

    public void setMsisdn(String msisdn) {
        mMsisdn = msisdn;
    }

    public String getOperator() {
        return mOperator;
    }

    public void setOperator(String operator) {
        mOperator = operator;
    }

}
