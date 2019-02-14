package com.cloudwell.paywell.services.activity.notification.model

import com.google.gson.annotations.SerializedName


class ResNotificationAPI {

    @SerializedName("detail_message")
    var mNotificationDetailMessage: List<NotificationDetailMessage> = mutableListOf();
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Long = 0
    @SerializedName("total_message")
    var totalMessage: Long = 0
    @SerializedName("unread_message")
    var unreadMessage: Long = 0

}
