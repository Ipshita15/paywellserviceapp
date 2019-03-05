package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model;

import com.google.gson.annotations.SerializedName;


public class Segment {

    @SerializedName("Airline")
    private Airline mAirline;
    @SerializedName("Baggage")
    private String mBaggage;
    @SerializedName("Destination")
    private Destination mDestination;
    @SerializedName("Equipment")
    private String mEquipment;
    @SerializedName("JourneyDuration")
    private String mJourneyDuration;
    @SerializedName("Origin")
    private Origin mOrigin;
    @SerializedName("StopQuantity")
    private String mStopQuantity;
    @SerializedName("TripIndicator")
    private String mTripIndicator;

    public Airline getAirline() {
        return mAirline;
    }

    public void setAirline(Airline airline) {
        mAirline = airline;
    }

    public String getBaggage() {
        return mBaggage;
    }

    public void setBaggage(String baggage) {
        mBaggage = baggage;
    }

    public Destination getDestination() {
        return mDestination;
    }

    public void setDestination(Destination destination) {
        mDestination = destination;
    }

    public String getEquipment() {
        return mEquipment;
    }

    public void setEquipment(String equipment) {
        mEquipment = equipment;
    }

    public String getJourneyDuration() {
        return mJourneyDuration;
    }

    public void setJourneyDuration(String journeyDuration) {
        mJourneyDuration = journeyDuration;
    }

    public Origin getOrigin() {
        return mOrigin;
    }

    public void setOrigin(Origin origin) {
        mOrigin = origin;
    }

    public String getStopQuantity() {
        return mStopQuantity;
    }

    public void setStopQuantity(String stopQuantity) {
        mStopQuantity = stopQuantity;
    }

    public String getTripIndicator() {
        return mTripIndicator;
    }

    public void setTripIndicator(String tripIndicator) {
        mTripIndicator = tripIndicator;
    }

}
