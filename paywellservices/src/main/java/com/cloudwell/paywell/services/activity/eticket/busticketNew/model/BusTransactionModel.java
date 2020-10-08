package com.cloudwell.paywell.services.activity.eticket.busticketNew.model;

/**
 * Created by YASIN on 16,July,2019
 * Email: yasinenubd5@gmail.com
 */
public class BusTransactionModel {
    private String transactionDate;
    private String bookingId;
    private String bookingStatus;
    private String statusMessageForConfirm;
    private String webBookingId;
    private String ticketPrice;
    private String customerName;
    private String customerGender;
    private String customerPhone;
    private String ticketNum;
    private String boardingPoint;
    private String departureDate;
    private String departureTime;
    private String seatNum;
    private String coachNum;
    private String busName;
    private String travellingTo;
    private String travellingFrom;
    private String customerAddress;
    private String customerEmail;
    private String transactioID;

    private String message;

    public BusTransactionModel() {
    }

    public BusTransactionModel(String transactionDate, String transactioID, String bookingId, String bookingStatus, String statusMessageForConfirm, String webBookingId, String ticketPrice, String customerName, String customerGender, String customerPhone, String customerAddress, String customerEmail, String ticketNum, String boardingPoint, String departureDate, String departureTime, String seatNum, String coachNum, String busName, String travellingTo, String travellingFrom) {
        this.transactionDate = transactionDate;
        this.transactioID = transactioID;
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
        this.statusMessageForConfirm = statusMessageForConfirm;
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
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.ticketNum = ticketNum;
        this.boardingPoint = boardingPoint;
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

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
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

    public String getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(String ticketNum) {
        this.ticketNum = ticketNum;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getTransactioID() {
        return transactioID;
    }

    public void setTransactioID(String transactioID) {
        this.transactioID = transactioID;
    }

    public String getStatusMessageForConfirm() {
        return statusMessageForConfirm;
    }

    public void setStatusMessageForConfirm(String statusMessageForConfirm) {
        this.statusMessageForConfirm = statusMessageForConfirm;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
