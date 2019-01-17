
package com.cloudwell.paywell.services.service.notificaiton.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class APIResNoCheckNotification {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("unread")
    private String mUnread;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public String getUnread() {
        return mUnread;
    }

    public void setUnread(String unread) {
        mUnread = unread;
    }

}
