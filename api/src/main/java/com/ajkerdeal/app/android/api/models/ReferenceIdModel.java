package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rasel on 7/19/2016.
 */
public class ReferenceIdModel {
    @SerializedName("ReferenceId")
    private int ReferenceId;

    public ReferenceIdModel(int referenceId) {
        ReferenceId = referenceId;
    }

    public int getReferenceId() {
        return ReferenceId;
    }

    @Override
    public String toString() {
        return "ReferenceIdModel{" +
                "ReferenceId=" + ReferenceId +
                '}';
    }
}
