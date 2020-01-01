
package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BillDatum {

    @SerializedName("amount")
    private Double amount;
    @SerializedName("bill_no")
    private String billNo;

    @SerializedName("ref_id")
    private String refDd;


    public BillDatum(Double amount, String billNo, String refDd) {
        this.amount = amount;
        this.billNo = billNo;
        this.refDd = refDd;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public void setRefDd(String refDd) {
        this.refDd = refDd;
    }

    public String getRefDd() {
        return refDd;
    }
}
