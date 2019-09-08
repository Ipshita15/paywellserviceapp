package com.cloudwell.paywell.services.activity.eticket.busticketNew.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Data {


    @SerializedName("data")
    private List<Transport> mData;
    @SerializedName("status")
    private Long mStatus;

    public List<Transport> getData() {
        return mData;
    }

    public void setData(List<Transport> data) {
        mData = data;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
