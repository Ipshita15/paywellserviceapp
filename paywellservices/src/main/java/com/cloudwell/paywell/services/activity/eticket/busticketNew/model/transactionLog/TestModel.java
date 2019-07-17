
package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TestModel {

    @SerializedName("data")
    private String data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Long status;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

}
