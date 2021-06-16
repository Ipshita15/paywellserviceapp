package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Origin {

    @SerializedName("Airport")
    private Airport mAirport;
    @SerializedName("DepTime")
    private String mDepTime;

    public Airport getAirport() {
        return mAirport;
    }

    public void setAirport(Airport airport) {
        mAirport = airport;
    }

    public String getDepTime() {
        return mDepTime;
    }

    public void setDepTime(String depTime) {
        mDepTime = depTime;
    }

}
