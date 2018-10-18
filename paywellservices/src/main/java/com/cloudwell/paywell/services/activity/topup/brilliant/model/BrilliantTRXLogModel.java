package com.cloudwell.paywell.services.activity.topup.brilliant.model;

public class BrilliantTRXLogModel {

    private  String payWellTrxID;
    private  String brilliantTrxID;
    private  String brilliantNum;
    private  String amount;
    private  String status;
    private  String date;

    public BrilliantTRXLogModel(String payWellTrxID, String brilliantTrxID, String brilliantNum, String amount, String status, String date) {
        this.payWellTrxID = payWellTrxID;
        this.brilliantTrxID = brilliantTrxID;
        this.brilliantNum = brilliantNum;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public String getPayWellTrxID() {
        return payWellTrxID;
    }

    public void setPayWellTrxID(String payWellTrxID) {
        this.payWellTrxID = payWellTrxID;
    }

    public String getBrilliantTrxID() {
        return brilliantTrxID;
    }

    public void setBrilliantTrxID(String brilliantTrxID) {
        this.brilliantTrxID = brilliantTrxID;
    }

    public String getBrilliantNum() {
        return brilliantNum;
    }

    public void setBrilliantNum(String brilliantNum) {
        this.brilliantNum = brilliantNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
