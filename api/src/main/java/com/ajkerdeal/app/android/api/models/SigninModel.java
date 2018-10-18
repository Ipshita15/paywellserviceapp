package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by has9 on 5/19/16.
 */
public class SigninModel {

    @SerializedName("Id")
    private int loginId;

    @SerializedName("Name")
    private String userName;

    @SerializedName("Email")
    private String email;

    @SerializedName("Password")
    private String password;

    @SerializedName("Mobile")
    private String mobile;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("Address")
    private String address;

    @SerializedName("Location")
    private String location;

    @SerializedName("District")
    private String district;

    @SerializedName("KnowingSource")
    private String KnowingSource;

    @SerializedName("LocationBng")
    private String LocationBng;

    @SerializedName("AreaId")
    private int AreaId;

    @SerializedName("DistrictId")
    private int DistrictId;
    public SigninModel(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public SigninModel(int loginId,String userName, String email, String mobile, String gender, String address, String location, String district) {
        this.loginId = loginId;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.address = address;
        this.location = location;
        this.district = district;
        this.userName=userName;
    }

    public SigninModel(int loginId, String userName, String email, String mobile, String gender, String address, String location, String district, String knowingSource, String locationBng, int areaId, int districtId) {
        this.loginId = loginId;
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.address = address;
        this.location = location;
        this.district = district;
        KnowingSource = knowingSource;
        LocationBng = locationBng;
        AreaId = areaId;
        DistrictId = districtId;
    }

    public int getId()
    {
        return loginId;
    }
    public String getUserName(){
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public String getDistrict() {return district;}

    public String getKnowingSource() {return KnowingSource;}

    public String getLocationBng() {return LocationBng;}

    public int getAreaId() {return AreaId;}

    public int getDistrictId() {
        return DistrictId;
    }

    @Override
    public String toString() {
        return "SigninModel{" +
                "loginId=" + loginId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", district='" + district + '\'' +
                ", KnowingSource='" + KnowingSource + '\'' +
                ", LocationBng='" + LocationBng + '\'' +
                ", AreaId=" + AreaId +
                ", DistrictId=" + DistrictId +
                '}';
    }
}
