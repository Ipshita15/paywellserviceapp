package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 6/22/16.
 */
public class UpdateUserInformationModel {
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Mobile")
    private String Mobile;
    @SerializedName("LocationId")
    private int LocationId;
    @SerializedName("Address")
    private String Address;
    @SerializedName("DistrictId")
    private int DistrictId;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("KnowingSource")
    private String KnowingSource;

    public UpdateUserInformationModel(int customerId, String name, String mobile, int locationId,
                                      String address, int districtId, String gender, String knowingSource) {
        CustomerId = customerId;
        Name = name;
        Mobile = mobile;
        LocationId = locationId;
        Address = address;
        DistrictId = districtId;
        Gender = gender;
        KnowingSource = knowingSource;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public int getLocationId() {
        return LocationId;
    }

    public String getAddress() {
        return Address;
    }

    public int getDistrictId() {
        return DistrictId;
    }

    public String getGender() {
        return Gender;
    }

    public String getKnowingSource() {
        return KnowingSource;
    }

    @Override
    public String toString() {
        return "UpdateUserInformationModel{" +
                "CustomerId=" + CustomerId +
                ", Name='" + Name + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", LocationId=" + LocationId +
                ", Address='" + Address + '\'' +
                ", DistrictId=" + DistrictId +
                ", Gender='" + Gender + '\'' +
                ", KnowingSource='" + KnowingSource + '\'' +
                '}';
    }
}
