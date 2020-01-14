
package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model;

import com.google.gson.annotations.SerializedName;



public class DescoBillPaySubmitResponse {

    @SerializedName("ApiStatus")
    private Long mApiStatus;
    @SerializedName("ApiStatusName")
    private String mApiStatusName;
    @SerializedName("ResponseDetails")
    private ResponseDetails mResponseDetails;

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

    public ResponseDetails getResponseDetails() {
        return mResponseDetails;
    }

    public void setResponseDetails(ResponseDetails responseDetails) {
        mResponseDetails = responseDetails;
    }

}
