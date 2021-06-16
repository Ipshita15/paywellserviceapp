package com.cloudwell.paywell.services.activity.notification.model.getNotification

import com.google.gson.annotations.SerializedName
class RequestNotificationAll {
    @SerializedName("format")
    var format: String = ""
    @SerializedName("mes_type")
    var mesType: String = ""
    @SerializedName("message_status")
    var messageStatus: String = ""
    @SerializedName("username")
    var username: String = ""

    @SerializedName("message_id")
    var message_id: String = ""

}