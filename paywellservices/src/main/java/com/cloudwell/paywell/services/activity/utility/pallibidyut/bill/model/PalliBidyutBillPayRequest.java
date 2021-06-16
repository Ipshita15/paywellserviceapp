
package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PalliBidyutBillPayRequest {

    @SerializedName("request_data")
    private List<BillDatum> billData;
    @SerializedName("format")
    private String format;
    @SerializedName("password")
    private String pinCode;
    @SerializedName("username")
    private String username;

    public PalliBidyutBillPayRequest(List<BillDatum> billData, String format, String pinCode, String username) {
        this.billData = billData;
        this.format = format;
        this.pinCode = pinCode;
        this.username = username;
    }

    public List<BillDatum> getBillData() {
        return billData;
    }

    public void setBillData(List<BillDatum> billData) {
        this.billData = billData;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
