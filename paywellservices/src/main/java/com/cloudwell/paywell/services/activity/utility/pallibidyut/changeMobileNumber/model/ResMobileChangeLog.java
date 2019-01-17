package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 16/1/19.
 */
public class ResMobileChangeLog {

    @SerializedName("trxId")
    private String trxId;

    @SerializedName("customer_acc_no")

    private String customerAccNo;
    @SerializedName("customer_phn")
    @Expose
    private String customerPhn;
    @SerializedName("request_datetime")
    @Expose
    private String requestDatetime;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("statusName")
    @Expose
    private String statusName;

    @SerializedName("response_details")
    @Expose
    private String responseDetails;

    public String getCustomerAccNo() {
        return customerAccNo;
    }

    public void setCustomerAccNo(String customerAccNo) {
        this.customerAccNo = customerAccNo;
    }

    public String getCustomerPhn() {
        return customerPhn;
    }

    public void setCustomerPhn(String customerPhn) {
        this.customerPhn = customerPhn;
    }

    public String getRequestDatetime() {
        return requestDatetime;
    }

    public void setRequestDatetime(String requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }
}
