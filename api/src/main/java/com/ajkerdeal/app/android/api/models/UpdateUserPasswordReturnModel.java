package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 6/21/16.
 */
public class UpdateUserPasswordReturnModel {
    @SerializedName("IsPasswordChanged")
    private boolean IsPasswordChanged;

    public UpdateUserPasswordReturnModel(boolean isPasswordChanged) {
        IsPasswordChanged = isPasswordChanged;
    }

    public boolean isPasswordChanged() {
        return IsPasswordChanged;
    }

    @Override
    public String toString() {
        return "UpdateUserPasswordReturnModel{" +
                "IsPasswordChanged=" + IsPasswordChanged +
                '}';
    }
}
