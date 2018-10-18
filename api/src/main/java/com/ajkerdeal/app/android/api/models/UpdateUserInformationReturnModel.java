package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 6/22/16.
 */
public class UpdateUserInformationReturnModel {
    @SerializedName("Count")
    private int Count;

    public UpdateUserInformationReturnModel(int count) {
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    @Override
    public String toString() {
        return "UpdateUserInformationReturnModel{" +
                "Count=" + Count +
                '}';
    }
}
