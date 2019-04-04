package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class Result {

    @SerializedName("Availabilty")
    private Double mAvailabilty;
    @SerializedName("Currency")
    private String mCurrency;
    @SerializedName("Discount")
    private Double mDiscount;
    @SerializedName("ExtraServices")
    private Object mExtraServices;
    @SerializedName("FareType")
    private String mFareType;
    @SerializedName("Fares")
    private List<Fare> mFares;
    @SerializedName("IsRefundable")
    private Boolean mIsRefundable;
    @SerializedName("LastTicketDate")
    private String mLastTicketDate;
    @SerializedName("PassportMadatory")
    private Boolean mPassportMadatory = false;
    @SerializedName("ResultID")
    private String mResultID;
    @SerializedName("segments")
    private List<Segment> mSegments;
    @SerializedName("TotalFare")
    private Double mTotalFare;
    @SerializedName("TotalFareWithAgentMarkup")
    private Double mTotalFareWithAgentMarkup;
    @SerializedName("Validatingcarrier")
    private String mValidatingcarrier;

    public Double getAvailabilty() {
        return mAvailabilty;
    }

    public void setAvailabilty(Double availabilty) {
        mAvailabilty = availabilty;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public double getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Double discount) {
        mDiscount = discount;
    }

    public Object getExtraServices() {
        return mExtraServices;
    }

    public void setExtraServices(Object extraServices) {
        mExtraServices = extraServices;
    }

    public String getFareType() {
        return mFareType;
    }

    public void setFareType(String fareType) {
        mFareType = fareType;
    }

    public List<Fare> getFares() {
        return mFares;
    }

    public void setFares(List<Fare> fares) {
        mFares = fares;
    }

    public Boolean getIsRefundable() {
        return mIsRefundable;
    }

    public void setIsRefundable(Boolean isRefundable) {
        mIsRefundable = isRefundable;
    }

    public String getLastTicketDate() {
        return mLastTicketDate;
    }

    public void setLastTicketDate(String lastTicketDate) {
        mLastTicketDate = lastTicketDate;
    }

    public Boolean getPassportMadatory() {
        return mPassportMadatory;
    }

    public void setPassportMadatory(Boolean passportMadatory) {
        mPassportMadatory = passportMadatory;
    }

    public String getResultID() {
        return mResultID;
    }

    public void setResultID(String resultID) {
        mResultID = resultID;
    }

    public List<Segment> getSegments() {
        return mSegments;
    }

    public void setSegments(List<Segment> segments) {
        mSegments = segments;
    }

    public Double getTotalFare() {
        return mTotalFare;
    }

    public void setTotalFare(Double totalFare) {
        mTotalFare = totalFare;
    }

    public Double getTotalFareWithAgentMarkup() {
        return mTotalFareWithAgentMarkup;
    }

    public void setTotalFareWithAgentMarkup(Double totalFareWithAgentMarkup) {
        mTotalFareWithAgentMarkup = totalFareWithAgentMarkup;
    }

    public String getValidatingcarrier() {
        return mValidatingcarrier;
    }

    public void setValidatingcarrier(String validatingcarrier) {
        mValidatingcarrier = validatingcarrier;
    }

}
