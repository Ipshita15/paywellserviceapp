
package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ResponseDetail {

    @SerializedName("BillAmount")
    private Double billAmount;
    @SerializedName("BillNo")
    private String billNo;
    @SerializedName("ExtraCharge")
    private Double extraCharge;
    @SerializedName("Status")
    private Long status;
    @SerializedName("StatusName")
    private String statusName;
    @SerializedName("TotalAmount")
    private Double totalAmount;
    @SerializedName("TrxId")
    private String trxId;
    @SerializedName("RetailerCommission")
    private String RetailerCommission;

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Double getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Double extraCharge) {
        this.extraCharge = extraCharge;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getRetailerCommission() {
        return RetailerCommission;
    }

    public void setRetailerCommission(String retailerCommission) {
        RetailerCommission = retailerCommission;
    }
}
