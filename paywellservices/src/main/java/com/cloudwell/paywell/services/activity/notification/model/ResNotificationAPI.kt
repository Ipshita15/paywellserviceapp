package com.cloudwell.paywell.services.activity.notification.model

import com.google.gson.annotations.SerializedName


class ResNotificationAPI {

    @SerializedName("detail_message")
    var mNotificationDetailMessage: List<NotificationDetailMessage>? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("status")
    var status: Long? = null
    @SerializedName("total_message")
    var totalMessage: Long? = null
    @SerializedName("unread_message")
    var unreadMessage: Long? = null

}
