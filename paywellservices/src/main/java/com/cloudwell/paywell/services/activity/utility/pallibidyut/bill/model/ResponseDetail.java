
package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ResponseDetail {

    @SerializedName("BillAmount")
    private Long billAmount;
    @SerializedName("BillNo")
    private String billNo;
    @SerializedName("ExtraCharge")
    private Long extraCharge;
    @SerializedName("Status")
    private Long status;
    @SerializedName("StatusName")
    private String statusName;
    @SerializedName("TotalAmount")
    private Long totalAmount;
    @SerializedName("TrxId")
    private String trxId;

    public Long getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Long billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Long getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Long extraCharge) {
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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

}
