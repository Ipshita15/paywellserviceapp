package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Fare {

    @SerializedName("AgentMarkUp")
    private Long mAgentMarkUp;
    @SerializedName("BaseFare")
    private Long mBaseFare;
    @SerializedName("Currency")
    private String mCurrency;
    @SerializedName("Discount")
    private Long mDiscount;
    @SerializedName("OtherCharges")
    private Long mOtherCharges;
    @SerializedName("PassengerCount")
    private Long mPassengerCount;
    @SerializedName("PaxType")
    private String mPaxType;
    @SerializedName("ServiceFee")
    private Long mServiceFee;
    @SerializedName("Tax")
    private Long mTax;

    public Long getAgentMarkUp() {
        return mAgentMarkUp;
    }

    public void setAgentMarkUp(Long agentMarkUp) {
        mAgentMarkUp = agentMarkUp;
    }

    public Long getBaseFare() {
        return mBaseFare;
    }

    public void setBaseFare(Long baseFare) {
        mBaseFare = baseFare;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public Long getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Long discount) {
        mDiscount = discount;
    }

    public Long getOtherCharges() {
        return mOtherCharges;
    }

    public void setOtherCharges(Long otherCharges) {
        mOtherCharges = otherCharges;
    }

    public Long getPassengerCount() {
        return mPassengerCount;
    }

    public void setPassengerCount(Long passengerCount) {
        mPassengerCount = passengerCount;
    }

    public String getPaxType() {
        return mPaxType;
    }

    public void setPaxType(String paxType) {
        mPaxType = paxType;
    }

    public Long getServiceFee() {
        return mServiceFee;
    }

    public void setServiceFee(Long serviceFee) {
        mServiceFee = serviceFee;
    }

    public Long getTax() {
        return mTax;
    }

    public void setTax(Long tax) {
        mTax = tax;
    }

}
