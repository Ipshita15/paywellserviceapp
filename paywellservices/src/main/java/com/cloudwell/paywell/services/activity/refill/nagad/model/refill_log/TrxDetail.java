
package com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class TrxDetail {

    @Expose
    @SerializedName("amount")
    private String amount;
    @Expose
    @SerializedName("sender")
    private String sender;
    @Expose
    @SerializedName("status")
    private String status;
    @SerializedName("trx_id")
    private String trxId;
    @SerializedName("trx_reference")
    private String trxReference;
    @SerializedName("trx_timestamp")
    private String trxTimestamp;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getTrxReference() {
        return trxReference;
    }

    public void setTrxReference(String trxReference) {
        this.trxReference = trxReference;
    }

    public String getTrxTimestamp() {
        return trxTimestamp;
    }

    public void setTrxTimestamp(String trxTimestamp) {
        this.trxTimestamp = trxTimestamp;
    }

}
