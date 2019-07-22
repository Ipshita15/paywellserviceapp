package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("busInfo")
    private BusInfo busInfo;
    @SerializedName("customerInfo")
    private CustomerInfo customerInfo;
    @SerializedName("statusCode")
    private String statusCode;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("statusMessageForConfirm")
    private Object statusMessageForConfirm;
    @SerializedName("ticketInfo")
    private TicketInfo ticketInfo;
    @SerializedName("transactionDateTime")
    private String transactionDateTime;

    @SerializedName("trxId")
    private String trxId;

    public BusInfo getBusInfo() {
        return busInfo;
    }

    public void setBusInfo(BusInfo busInfo) {
        this.busInfo = busInfo;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Object getStatusMessageForConfirm() {
        return statusMessageForConfirm;
    }

    public void setStatusMessageForConfirm(Object statusMessageForConfirm) {
        this.statusMessageForConfirm = statusMessageForConfirm;
    }

    public TicketInfo getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(TicketInfo ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }
}
