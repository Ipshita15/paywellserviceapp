
package com.cloudwell.paywell.services.activity.eticket.airticket.source.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AirPortsData {

    @SerializedName("airports")
    private List<Airport> mAirports;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;

    public List<Airport> getAirports() {
        return mAirports;
    }

    public void setAirports(List<Airport> airports) {
        mAirports = airports;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
