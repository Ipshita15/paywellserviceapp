package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResCancellationMapping {

    @SerializedName("booking_data")
    private List<BookingDatum> mBookingData;
    @SerializedName("cancel_data")
    private CancelData mCancelData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private int mStatus;

    public List<BookingDatum> getBookingData() {
        return mBookingData;
    }

    public void setBookingData(List<BookingDatum> bookingData) {
        mBookingData = bookingData;
    }

    public CancelData getCancelData() {
        return mCancelData;
    }

    public void setCancelData(CancelData cancelData) {
        mCancelData = cancelData;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


}
