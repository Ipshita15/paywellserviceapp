package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CustomerInfo {

    @SerializedName("customerAge")
    private Object customerAge;
    @SerializedName("customerEmail")
    private Object customerEmail;
    @SerializedName("customerName")
    private Object customerName;
    @SerializedName("customerPhone")
    private Object customerPhone;

    public Object getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(Object customerAge) {
        this.customerAge = customerAge;
    }

    public Object getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(Object customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Object getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Object customerName) {
        this.customerName = customerName;
    }

    public Object getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(Object customerPhone) {
        this.customerPhone = customerPhone;
    }

}
