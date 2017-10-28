package com.cloudwell.paywell.services.activity.eticket.busticket.model;

import java.util.ArrayList;

public class SearchBus {
    private int coachConfigID;
    private String coachSeatPlanId;
    private String coachConfigSKU;
    private String coachNo;
    private String coachType;
    private String availableSeats;
    private String seatType;
    private String fare;
    private String optionID;
    private ArrayList<String> seatTypesID;
    private ArrayList<String> fares;
    private String seatPlanSKU;
    private String companyName;
    private String companySKU;
    private String routeName;
    private String startCounter;
    private String endCounter;
    private String arrivalTime;
    private String departureTime;

    public int getCoachConfigID() {
        return coachConfigID;
    }

    public void setCoachConfigID(int coachConfigID) {
        this.coachConfigID = coachConfigID;
    }

    public String getCoachSeatPlanId() {
        return coachSeatPlanId;
    }

    public void setCoachSeatPlanId(String coachSeatPlanId) {
        this.coachSeatPlanId = coachSeatPlanId;
    }

    public String getCoachConfigSKU() {
        return coachConfigSKU;
    }

    public void setCoachConfigSKU(String coachConfigSKU) {
        this.coachConfigSKU = coachConfigSKU;
    }

    public String getCoachNo() {
        return coachNo;
    }

    public void setCoachNo(String coachNo) {
        this.coachNo = coachNo;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getSeatsType() {
        return seatType;
    }

    public void setSeatsType(String seatType) {
        this.seatType = seatType;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getOptionID() {
        return optionID;
    }

    public void setOptionID(String optionID) {
        this.optionID = optionID;
    }

    public ArrayList<String> getSeatTypesID() {
        return seatTypesID;
    }

    public void setSeatTypesID(ArrayList<String> seatTypesID) {
        this.seatTypesID = seatTypesID;
    }

    public ArrayList<String> getFares() {
        return fares;
    }

    public void setFares(ArrayList<String> fares) {
        this.fares = fares;
    }

    public String getSeatPlanSKU() {
        return seatPlanSKU;
    }

    public void setSeatPlanSKU(String seatPlanSKU) {
        this.seatPlanSKU = seatPlanSKU;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySKU() {
        return companySKU;
    }

    public void setCompanySKU(String companySKU) {
        this.companySKU = companySKU;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartCounter() {
        return startCounter;
    }

    public void setStartCounter(String startCounter) {
        this.startCounter = startCounter;
    }

    public String getEndCounter() {
        return endCounter;
    }

    public void setEndCounter(String endCounter) {
        this.endCounter = endCounter;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
