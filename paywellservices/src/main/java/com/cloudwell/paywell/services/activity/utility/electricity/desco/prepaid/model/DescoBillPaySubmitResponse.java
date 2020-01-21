
package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model;

import com.google.gson.annotations.SerializedName;



public class DescoBillPaySubmitResponse {

    @SerializedName("ApiStatus")
    private Long mApiStatus;
    @SerializedName("ApiStatusName")
    private String mApiStatusName;
    @SerializedName("ResponseDetails")
    private DescoBillPaySubmitResponseDetails mDescoBillPaySubmitResponseDetails;

    public Long getApiStatus() {
        return mApiStatus;
    }

    public void setApiStatus(Long apiStatus) {
        mApiStatus = apiStatus;
    }

    public String getApiStatusName() {
        return mApiStatusName;
    }

    public void setApiStatusName(String apiStatusName) {
        mApiStatusName = apiStatusName;
    }

    public DescoBillPaySubmitResponseDetails getResponseDetails() {
        return mDescoBillPaySubmitResponseDetails;
    }

    public void setResponseDetails(DescoBillPaySubmitResponseDetails descoBillPaySubmitResponseDetails) {
        mDescoBillPaySubmitResponseDetails = descoBillPaySubmitResponseDetails;
    }

}
