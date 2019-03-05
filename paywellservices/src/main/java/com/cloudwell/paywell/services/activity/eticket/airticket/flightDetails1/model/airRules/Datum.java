package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules;

import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("AirlineCode")
    private Object mAirlineCode;
    @SerializedName("CityPair")
    private Object mCityPair;
    @SerializedName("Error")
    private Object mError;
    @SerializedName("Paxtype")
    private String mPaxtype;
    @SerializedName("RuleDetails")
    private String mRuleDetails;
    @SerializedName("RuleType")
    private String mRuleType;

    public Object getAirlineCode() {
        return mAirlineCode;
    }

    public void setAirlineCode(Object airlineCode) {
        mAirlineCode = airlineCode;
    }

    public Object getCityPair() {
        return mCityPair;
    }

    public void setCityPair(Object cityPair) {
        mCityPair = cityPair;
    }

    public Object getError() {
        return mError;
    }

    public void setError(Object error) {
        mError = error;
    }

    public String getPaxtype() {
        return mPaxtype;
    }

    public void setPaxtype(String paxtype) {
        mPaxtype = paxtype;
    }

    public String getRuleDetails() {
        return mRuleDetails;
    }

    public void setRuleDetails(String ruleDetails) {
        mRuleDetails = ruleDetails;
    }

    public String getRuleType() {
        return mRuleType;
    }

    public void setRuleType(String ruleType) {
        mRuleType = ruleType;
    }

}
