
package com.cloudwell.paywell.services.activity.eticket.airticket.source.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Airport {

    @SerializedName("airport_name")
    private String mAirportName;
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("iata")
    private String mIata;
    @SerializedName("icao")
    private String mIcao;
    @SerializedName("id")
    private String mId;
    @SerializedName("iso")
    private String mIso;
    @SerializedName("state")
    private String mState;
    @SerializedName("time_zone")
    private String mTimeZone;

    public String getAirportName() {
        return mAirportName;
    }

    public void setAirportName(String airportName) {
        mAirportName = airportName;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getIata() {
        return mIata;
    }

    public void setIata(String iata) {
        mIata = iata;
    }

    public String getIcao() {
        return mIcao;
    }

    public void setIcao(String icao) {
        mIcao = icao;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getIso() {
        return mIso;
    }

    public void setIso(String iso) {
        mIso = iso;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

}
