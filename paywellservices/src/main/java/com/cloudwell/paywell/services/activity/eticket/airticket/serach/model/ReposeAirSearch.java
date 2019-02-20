package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model;

import com.google.gson.annotations.SerializedName;


public class ReposeAirSearch {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private int mStatus;
    private Throwable mThrowable;

    public ReposeAirSearch(Throwable t) {
        mThrowable = t;
    }

    public ReposeAirSearch() {

    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public void setStatus(Integer status) {
        mStatus = status;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }
}
