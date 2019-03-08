package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Airport {

    @SerializedName("AirportCode")
    private String mAirportCode;
    @SerializedName("AirportName")
    private String mAirportName;
    @SerializedName("CityCode")
    private String mCityCode;
    @SerializedName("CityName")
    private String mCityName;
    @SerializedName("CountryCode")
    private String mCountryCode;
    @SerializedName("CountryName")
    private String mCountryName;
    @SerializedName("Terminal")
    private String mTerminal;

    public String getAirportCode() {
        return mAirportCode;
    }

    public void setAirportCode(String airportCode) {
        mAirportCode = airportCode;
    }

    public String getAirportName() {
        return mAirportName;
    }

    public void setAirportName(String airportName) {
        mAirportName = airportName;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }

    public String getTerminal() {
        return mTerminal;
    }

    public void setTerminal(String terminal) {
        mTerminal = terminal;
    }

}
