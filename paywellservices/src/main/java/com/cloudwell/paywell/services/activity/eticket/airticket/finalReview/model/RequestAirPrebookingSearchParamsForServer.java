package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestAirPrebookingSearchParamsForServer {

    @SerializedName("Passengers")
    private List<PassengerForAPI> mPassengers;
    @SerializedName("ResultID")
    private String mResultID;
    @SerializedName("SearchId")
    private String mSearchId;

    public List<PassengerForAPI> getPassengers() {
        return mPassengers;
    }

    public void setPassengers(List<PassengerForAPI> passengers) {
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
