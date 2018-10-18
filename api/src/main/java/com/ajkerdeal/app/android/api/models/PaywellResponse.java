package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sumaya on 10-Oct-17.
 */

public class PaywellResponse {
    @SerializedName("IsSuccess")
    private boolean mIsSuccess;
    @SerializedName("Message")
    private String mMessage;

    public PaywellResponse() {
    }

    public PaywellResponse(boolean mIsSuccess, String mMessage) {
        this.mIsSuccess = mIsSuccess;
        this.mMessage = mMessage;
    }

    public boolean ismIsSuccess() {
        return mIsSuccess;
    }

    public String getmMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return "PaywellResponse{" +
                "mIsSuccess=" + mIsSuccess +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }
}
