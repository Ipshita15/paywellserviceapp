
package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class TicketInfo {

    @Expose
    private String boardingPointName;
    @Expose
    private String bookingInfoId;
    @Expose
    private String departureDate;
    @Expose
    private String departureTime;
    @Expose
    private String journeyRoute;
    @Expose
    private String noOfSeat;
    @Expose
    private String seatLbls;
    @Expose
    private String ticketNo;
    @Expose
    private String totalAmount;
    @Expose
    private String webBookingInfoId;

    public String getBoardingPointName() {
        return boardingPointName;
    }

    public void setBoardingPointName(String boardingPointName) {
        this.boardingPointName = boardingPointName;
    }

    public String getBookingInfoId() {
        return bookingInfoId;
    }

    public void setBookingInfoId(String bookingInfoId) {
        this.bookingInfoId = bookingInfoId;
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

    public String getJourneyRoute() {
        return journeyRoute;
    }

    public void setJourneyRoute(String journeyRoute) {
        this.journeyRoute = journeyRoute;
    }

    public String getNoOfSeat() {
        return noOfSeat;
    }

    public void setNoOfSeat(String noOfSeat) {
        this.noOfSeat = noOfSeat;
    }

    public String getSeatLbls() {
        return seatLbls;
    }

    public void setSeatLbls(String seatLbls) {
        this.seatLbls = seatLbls;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getWebBookingInfoId() {
        return webBookingInfoId;
    }

    public void setWebBookingInfoId(String webBookingInfoId) {
        this.webBookingInfoId = webBookingInfoId;
    }

}
