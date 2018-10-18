package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitu on 7/19/16.
 */
public class LocationIdModel {
    @SerializedName("AreaID")
    private int AreaID;
    @SerializedName("Location")
    private String Location;
    @SerializedName("LocationBng")
    private String LocationBng;

    public LocationIdModel(int areaID, String location, String locationBng) {
        AreaID = areaID;
        Location = location;
        LocationBng = locationBng;
    }

    public int getAreaID() {
        return AreaID;
    }

    public String getLocation() {
        return Location;
    }

    public String getLocationBng() {
        return LocationBng;
    }

    @Override
    public String toString() {
        return "LocationIdModel{" +
                "AreaID=" + AreaID +
                ", Location='" + Location + '\'' +
                ", LocationBng='" + LocationBng + '\'' +
                '}';
    }
}
