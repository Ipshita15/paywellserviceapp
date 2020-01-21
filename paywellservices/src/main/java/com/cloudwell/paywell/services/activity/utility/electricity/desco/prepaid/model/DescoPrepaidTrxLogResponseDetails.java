
package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DescoPrepaidTrxLogResponseDetails implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("msg_text")
    private List<MsgText> msgText;
    @SerializedName("noOfTrx")
    private Long noOfTrx;
    @SerializedName("status")
    private Long status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MsgText> getMsgText() {
        return msgText;
    }

    public void setMsgText(List<MsgText> msgText) {
        this.msgText = msgText;
    }

    public Long getNoOfTrx() {
        return noOfTrx;
    }

    public void setNoOfTrx(Long noOfTrx) {
        this.noOfTrx = noOfTrx;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

}
