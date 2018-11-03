package com.cloudwell.paywell.services.activity.topup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 11/3/18.
 */
public class TopupData {

    @SerializedName("msisdn")
    @Expose
    private String msisdn;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("con_type")
    @Expose
    private String conType;
    @SerializedName("operator")
    @Expose
    private String operator;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getConType() {
        return conType;
    }

    public void setConType(String conType) {
        this.conType = conType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
