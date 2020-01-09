
package com.cloudwell.paywell.services.activity.topup.model;

import com.cloudwell.paywell.services.utils.ParameterUtility;
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

    @SerializedName(ParameterUtility.KEY_REF_ID)
    private String refId;

    public TopupData(String amount, String conType, String msisdn, String operator, String refId) {
        mAmount = amount;
        mConType = conType;
        mMsisdn = msisdn;
        mOperator = operator;
        this.refId = refId;
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

    public void setRefDd(String refDd) {
        this.refId = refDd;
    }

    public String getRefDd() {
        return refId;
    }

}
