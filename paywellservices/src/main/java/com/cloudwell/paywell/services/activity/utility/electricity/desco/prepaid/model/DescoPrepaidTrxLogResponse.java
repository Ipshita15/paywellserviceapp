
package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DescoPrepaidTrxLogResponse implements Serializable {

    @SerializedName("ApiStatus")
    private Long apiStatus;
    @SerializedName("ApiStatusName")
    private String apiStatusName;
    @SerializedName("ResponseDetails")
    private DescoPrepaidTrxLogResponseDetails descoPrepaidTrxLogResponseDetails;

    public Long getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(Long apiStatus) {
        this.apiStatus = apiStatus;
    }

    public String getApiStatusName() {
        return apiStatusName;
    }

    public void setApiStatusName(String apiStatusName) {
        this.apiStatusName = apiStatusName;
    }

    public DescoPrepaidTrxLogResponseDetails getDescoPrepaidTrxLogResponseDetails() {
        return descoPrepaidTrxLogResponseDetails;
    }

    public void setDescoPrepaidTrxLogResponseDetails(DescoPrepaidTrxLogResponseDetails descoPrepaidTrxLogResponseDetails) {
        this.descoPrepaidTrxLogResponseDetails = descoPrepaidTrxLogResponseDetails;
    }

}
