package com.cloudwell.paywell.services.activity.eticket.busticketNew.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Data {


    @SerializedName("data")
    private List<Bus> mData;
    @SerializedName("status")
    private Long mStatus;

    public List<Bus> getData() {
        return mData;
    }

    public void setData(List<Bus> data) {
        mData = data;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
