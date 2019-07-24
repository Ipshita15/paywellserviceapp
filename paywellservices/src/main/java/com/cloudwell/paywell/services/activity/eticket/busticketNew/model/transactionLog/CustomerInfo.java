package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CustomerInfo {

    @SerializedName("customerAge")
    private String customerAge;
    @SerializedName("customerEmail")
    private String customerEmail;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("customerPhone")
    private String customerPhone;

    @SerializedName("customerGender")
    private String customerGender;
    @SerializedName("customerAddress")
    private String customerAddress;


    public String getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(String customerAge) {
        this.customerAge = customerAge;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCusTomerGenger() {
        return customerGender;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
