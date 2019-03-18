package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model;

import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestAirPrebookingSearchParams {

    @SerializedName("Passengers")
    private List<Passenger> mPassengers;
    @SerializedName("ResultID")
    private String mResultID;
    @SerializedName("SearchId")
    private String mSearchId;

    public List<Passenger> getPassengers() {
        return mPassengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        mPassengers = passengers;
    }

    public String getResultID() {
        return mResultID;
    }

    public void setResultID(String resultID) {
        mResultID = resultID;
    }

    public String getSearchId() {
        return mSearchId;
    }

    public void setSearchId(String searchId) {
        mSearchId = searchId;
    }

}
