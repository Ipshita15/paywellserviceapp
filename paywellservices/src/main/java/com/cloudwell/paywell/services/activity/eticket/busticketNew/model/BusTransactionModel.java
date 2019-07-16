package com.cloudwell.paywell.services.activity.eticket.busticketNew.model;

/**
 * Created by YASIN on 16,July,2019
 * Email: yasinenubd5@gmail.com
 */
public class BusTransactionModel {
    private String transactionDate;
    private String bookingId;
    private String bookingStatus;
    private String webBookingId;
    private String ticketPrice;
    private String customerName;
    private String customerGender;
    private String departureDate;
    private String departureTime;
    private String seatNum;
    private String coachNum;
    private String busName;
    private String travellingTo;
    private String travellingFrom;

    public BusTransactionModel(String transactionDate, String bookingId, String bookingStatus, String webBookingId, String ticketPrice, String customerName, String customerGender, String departureDate, String departureTime, String seatNum, String coachNum, String busName, String travellingTo, String travellingFrom) {
        this.transactionDate = transactionDate;
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
        this.webBookingId = webBookingId;
        this.ticketPrice = ticketPrice;
        this.customerName = customerName;
        this.customerGender = customerGender;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.seatNum = seatNum;
        this.coachNum = coachNum;
        this.busName = busName;
        this.travellingTo = travellingTo;
        this.travellingFrom = travellingFrom;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getWebBookingId() {
        return webBookingId;
    }

    public void setWebBookingId(String webBookingId) {
        this.webBookingId = webBookingId;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public String getCoachNum() {
        return coachNum;
    }

    public void setCoachNum(String coachNum) {
        this.coachNum = coachNum;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getTravellingTo() {
        return travellingTo;
    }

    public void setTravellingTo(String travellingTo) {
        this.travellingTo = travellingTo;
    }

    public String getTravellingFrom() {
        return travellingFrom;
    }

    public void setTravellingFrom(String travellingFrom) {
        this.travellingFrom = travellingFrom;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
