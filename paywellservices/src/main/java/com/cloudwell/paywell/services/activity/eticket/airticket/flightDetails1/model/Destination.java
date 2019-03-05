package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model;

import com.google.gson.annotations.SerializedName;


public class Destination {

    @SerializedName("Airport")
    private Airport mAirport;
    @SerializedName("ArrTime")
    private String mArrTime;

    public Airport getAirport() {
        return mAirport;
    }

    public void setAirport(Airport airport) {
        mAirport = airport;
    }

    public String getArrTime() {
        return mArrTime;
    }

    public void setArrTime(String arrTime) {
        mArrTime = arrTime;
    }

}
