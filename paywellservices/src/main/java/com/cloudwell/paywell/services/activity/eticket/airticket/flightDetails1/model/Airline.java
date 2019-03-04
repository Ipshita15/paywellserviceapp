package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Airline {

    @SerializedName("AirlineCode")
    private String mAirlineCode;
    @SerializedName("AirlineName")
    private String mAirlineName;
    @SerializedName("BookingClass")
    private String mBookingClass;
    @SerializedName("CabinClass")
    private String mCabinClass;
    @SerializedName("FlightNumber")
    private String mFlightNumber;
    @SerializedName("OperatingCarrier")
    private String mOperatingCarrier;

    public String getAirlineCode() {
        return mAirlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        mAirlineCode = airlineCode;
    }

    public String getAirlineName() {
        return mAirlineName;
    }

    public void setAirlineName(String airlineName) {
        mAirlineName = airlineName;
    }

    public String getBookingClass() {
        return mBookingClass;
    }

    public void setBookingClass(String bookingClass) {
        mBookingClass = bookingClass;
    }

    public String getCabinClass() {
        return mCabinClass;
    }

    public void setCabinClass(String cabinClass) {
        mCabinClass = cabinClass;
    }

    public String getFlightNumber() {
        return mFlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        mFlightNumber = flightNumber;
    }

    public String getOperatingCarrier() {
        return mOperatingCarrier;
    }

    public void setOperatingCarrier(String operatingCarrier) {
        mOperatingCarrier = operatingCarrier;
    }

}
