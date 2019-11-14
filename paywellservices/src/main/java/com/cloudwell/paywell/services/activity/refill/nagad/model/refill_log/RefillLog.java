
package com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log;

import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class RefillLog {

    @SerializedName("NoOfTrx")
    private Long noOfTrx;
    @SerializedName("Status")
    private int status;
    @SerializedName("Trx Details")
    private List<TrxDetail> trxDetails;


    @SerializedName("Message")
    private String message;

    public Long getNoOfTrx() {
        return noOfTrx;
    }

    public void setNoOfTrx(Long noOfTrx) {
        this.noOfTrx = noOfTrx;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TrxDetail> getTrxDetails() {
        return trxDetails;
    }

    public void setTrxDetails(List<TrxDetail> trxDetails) {
        this.trxDetails = trxDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
