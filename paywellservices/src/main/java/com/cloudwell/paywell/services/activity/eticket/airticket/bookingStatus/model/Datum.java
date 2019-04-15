package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model;

import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Segment;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class Datum {

    @SerializedName("Availabilty")
    private Long mAvailabilty;
    @SerializedName("Currency")
    private String mCurrency;
    @SerializedName("Discount")
    private Double mDiscount;
    @SerializedName("FareType")
    private String mFareType;
    @SerializedName("Fares")
    private List<Fare> mFares;
    @SerializedName("IsRefundable")
    private Boolean mIsRefundable;
    @SerializedName("LastTicketDate")
    private Object mLastTicketDate;
    @SerializedName("ResultID")
    private String mResultID;
    @SerializedName("segments")
    private List<Segment> mSegments;
    @SerializedName("TotalFare")
    private Long mTotalFare;
    @SerializedName("TotalFareWithAgentMarkup")
    private Long mTotalFareWithAgentMarkup;
    @SerializedName("Validatingcarrier")
    private String mValidatingcarrier;

    public Long getAvailabilty() {
        return mAvailabilty;
    }

    public void setAvailabilty(Long availabilty) {
        mAvailabilty = availabilty;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public Double getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Double discount) {
        mDiscount = discount;
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

    public Object getLastTicketDate() {
        return mLastTicketDate;
    }

    public void setLastTicketDate(Object lastTicketDate) {
        mLastTicketDate = lastTicketDate;
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

    public Long getTotalFare() {
        return mTotalFare;
    }

    public void setTotalFare(Long totalFare) {
        mTotalFare = totalFare;
    }

    public Long getTotalFareWithAgentMarkup() {
        return mTotalFareWithAgentMarkup;
    }

    public void setTotalFareWithAgentMarkup(Long totalFareWithAgentMarkup) {
        mTotalFareWithAgentMarkup = totalFareWithAgentMarkup;
    }

    public String getValidatingcarrier() {
        return mValidatingcarrier;
    }

    public void setValidatingcarrier(String validatingcarrier) {
        mValidatingcarrier = validatingcarrier;
    }

}
