package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by has9 on 5/21/16.
 */
public class SignUpModel {

    @SerializedName("Name")
    private String signUpName;

    @SerializedName("Email")
    private String signUpEmail;

    @SerializedName("Password")
    private String signUpPassword;

    @SerializedName("Mobile")
    private String signUpMobile;

    @SerializedName("Gender")
    private String signUpGender;

    @SerializedName("Address")
    private String signUpAddress;

    @SerializedName("LocationId")
    private int signUpLocationId;

    @SerializedName("DistrictId")
    private int signUpDistrictId;


    public SignUpModel(String signUpName, String signUpEmail, String signUpPassword, String signUpMobile, String signUpGender, String signUpAddress, int signUpLocationId, int signUpDistrictId) {
        this.signUpName = signUpName;
        this.signUpEmail = signUpEmail;
        this.signUpPassword = signUpPassword;
        this.signUpMobile = signUpMobile;
        this.signUpGender = signUpGender;
        this.signUpAddress = signUpAddress;
        this.signUpLocationId = signUpLocationId;
        this.signUpDistrictId = signUpDistrictId;
    }

    public String getSignUpName() {
        return signUpName;
    }

    public void setSignUpName(String signUpName) {
        this.signUpName = signUpName;
    }

    public String getSignUpEmail() {
        return signUpEmail;
    }

    public void setSignUpEmail(String signUpEmail) {
        this.signUpEmail = signUpEmail;
    }

    public void setSignUpPassword(String signUpPassword) {
        this.signUpPassword = signUpPassword;
    }

    public String getSignUpMobile() {
        return signUpMobile;
    }

    public void setSignUpMobile(String signUpMobile) {
        this.signUpMobile = signUpMobile;
    }

    public String getSignUpGender() {
        return signUpGender;
    }

    public void setSignUpGender(String signUpGender) {
        this.signUpGender = signUpGender;
    }

    public String getSignUpAddress() {
        return signUpAddress;
    }

    public void setSignUpAddress(String signUpAddress) {
        this.signUpAddress = signUpAddress;
    }

    public int getSignUpLocationId() {
        return signUpLocationId;
    }

    public void setSignUpLocationId(int signUpLocationId) {
        this.signUpLocationId = signUpLocationId;
    }

    public int getSignUpDistrictId() {
        return signUpDistrictId;
    }

    public void setSignUpDistrictId(int signUpDistrictId) {
        this.signUpDistrictId = signUpDistrictId;
    }

    @Override
    public String toString() {
        return "SignUpModel{" +
                "signUpName='" + signUpName + '\'' +
                ", signUpEmail='" + signUpEmail + '\'' +
                ", signUpPassword='" + signUpPassword + '\'' +
                ", signUpMobile='" + signUpMobile + '\'' +
                ", signUpGender='" + signUpGender + '\'' +
                ", signUpAddress='" + signUpAddress + '\'' +
                ", signUpLocationId=" + signUpLocationId +
                ", signUpDistrictId=" + signUpDistrictId +
                '}';
    }
}
