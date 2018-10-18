package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 5/12/16.
 */
public class BrandModel {

    private int BrandId;
    private String BrandName;


    public int getBandId() {
        return BrandId;
    }

    public String getBandName() {
        return BrandName;
    }

    public void setBandId(int BandId) {
        this.BrandId = BandId;
    }

    public void setBandName(String BandName) {
        this.BrandName = BandName;
    }

    @Override
    public String toString() {
        return "BandModel{" +
                "BandId=" + BrandId +
                ", BandName='" + BrandName + '\'' +
                '}';
    }
}
