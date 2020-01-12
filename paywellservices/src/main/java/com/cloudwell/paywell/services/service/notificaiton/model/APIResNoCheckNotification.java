
package com.cloudwell.paywell.services.service.notificaiton.model;

import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class APIResNoCheckNotification {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("unread")
    private String mUnread;

    @SerializedName("detail_message")
    private List<NotificationDetailMessage> detail_message;

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

    public List<NotificationDetailMessage> getDetail_message() {
        return detail_message;
    }

    public void setDetail_message(List<NotificationDetailMessage> detail_message) {
        this.detail_message = detail_message;
    }
}
