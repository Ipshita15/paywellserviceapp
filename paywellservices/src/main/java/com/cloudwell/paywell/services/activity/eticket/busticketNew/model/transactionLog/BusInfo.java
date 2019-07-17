package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BusInfo {

    @SerializedName("busLbls")
    private String busLbls;
    @SerializedName("busName")
    private String busName;
    @SerializedName("coach_no")
    private String coachNo;

    public String getBusLbls() {
        return busLbls;
    }

    public void setBusLbls(String busLbls) {
        this.busLbls = busLbls;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getCoachNo() {
        return coachNo;
    }

    public void setCoachNo(String coachNo) {
        this.coachNo = coachNo;
    }

}
