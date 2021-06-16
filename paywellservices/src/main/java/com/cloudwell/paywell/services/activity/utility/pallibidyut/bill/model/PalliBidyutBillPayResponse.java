
package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PalliBidyutBillPayResponse {

    @SerializedName("ApiStatus")
    private Long apiStatus;
    @SerializedName("ApiStatusName")
    private String apiStatusName;
    @SerializedName("CallCenter")
    private String callCenter;
    @SerializedName("OutletAddress")
    private String outletAddress;
    @SerializedName("OutletName")
    private String outletName;
    @SerializedName("ResponseDetails")
    private List<ResponseDetail> responseDetails;
    @SerializedName("RetailerCode")
    private String retailerCode;

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

    public String getCallCenter() {
        return callCenter;
    }

    public void setCallCenter(String callCenter) {
        this.callCenter = callCenter;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public List<ResponseDetail> getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(List<ResponseDetail> responseDetails) {
        this.responseDetails = responseDetails;
    }

    public String getRetailerCode() {
        return retailerCode;
    }

    public void setRetailerCode(String retailerCode) {
        this.retailerCode = retailerCode;
    }

}
