package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 6/21/16.
 */
public class UpdateUserPasswordModel {
    @SerializedName("OldPassword")
    private String OldPassword;
    @SerializedName("NewPassword")
    private String NewPassword;

    public UpdateUserPasswordModel(String oldPassword, String newPassword) {
        OldPassword = oldPassword;
        NewPassword = newPassword;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    @Override
    public String toString() {
        return "UpdateUserPasswordModel{" +
                "OldPassword='" + OldPassword + '\'' +
                ", NewPassword='" + NewPassword + '\'' +
                '}';
    }
}
