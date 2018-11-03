package com.cloudwell.paywell.services.activity.topup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 11/3/18.
 */
public class TopupResposeDatum {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("trans_id")
    @Expose
    private String transId;
    @SerializedName("topupData")
    @Expose
    private TopupData topupData;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public TopupData getTopupData() {
        return topupData;
    }

    public void setTopupData(TopupData topupData) {
        this.topupData = topupData;
    }


}
