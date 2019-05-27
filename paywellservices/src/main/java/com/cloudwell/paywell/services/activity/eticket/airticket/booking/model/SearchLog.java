package com.cloudwell.paywell.services.activity.eticket.airticket.booking.model;

import com.google.gson.annotations.SerializedName;


public class SearchLog {

    @SerializedName("adult_qty")
    private String mAdultQty;
    @SerializedName("cabin_class")
    private String mCabinClass;
    @SerializedName("child_qty")
    private String mChildQty;
    @SerializedName("departure_date_time")
    private String mDepartureDateTime;
    @SerializedName("destination_port")
    private String mDestinationPort;
    @SerializedName("id")
    private String mId;
    @SerializedName("infant_qty")
    private String mInfantQty;
    @SerializedName("origin_port")
    private String mOriginPort;
    @SerializedName("search_date_time")
    private String mSearchDateTime;
    @SerializedName("search_id")
    private String mSearchId;
    @SerializedName("user_id")
    private String mUserId;

    public String getAdultQty() {
        return mAdultQty;
    }

    public void setAdultQty(String adultQty) {
        mAdultQty = adultQty;
    }

    public String getCabinClass() {
        return mCabinClass;
    }

    public void setCabinClass(String cabinClass) {
        mCabinClass = cabinClass;
    }

    public String getChildQty() {
        return mChildQty;
    }

    public void setChildQty(String childQty) {
        mChildQty = childQty;
    }

    public String getDepartureDateTime() {
        return mDepartureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        mDepartureDateTime = departureDateTime;
    }

    public String getDestinationPort() {
        return mDestinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        mDestinationPort = destinationPort;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInfantQty() {
        return mInfantQty;
    }

    public void setInfantQty(String infantQty) {
        mInfantQty = infantQty;
    }

    public String getOriginPort() {
        return mOriginPort;
    }

    public void setOriginPort(String originPort) {
        mOriginPort = originPort;
    }

    public String getSearchDateTime() {
        return mSearchDateTime;
    }

    public void setSearchDateTime(String searchDateTime) {
        mSearchDateTime = searchDateTime;
    }

    public String getSearchId() {
        return mSearchId;
    }

    public void setSearchId(String searchId) {
        mSearchId = searchId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
