
package com.cloudwell.paywell.services.activity.eticket.airticket.booking.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public class BookingList {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
