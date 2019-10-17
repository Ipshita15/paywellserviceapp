
package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BillDatum {

    @SerializedName("amount")
    private Long amount;
    @SerializedName("bill_no")
    private String billNo;

    public BillDatum(Long amount, String billNo) {
        this.amount = amount;
        this.billNo = billNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

}
