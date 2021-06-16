package com.cloudwell.paywell.services.service.notificaiton.model.requestNotificationDetails

import com.google.gson.annotations.SerializedName

class RequestNotification {
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("username")
    var username: String = ""
    @SerializedName("deviceFcmToken")
    var deviceFcmToken: String = ""

}