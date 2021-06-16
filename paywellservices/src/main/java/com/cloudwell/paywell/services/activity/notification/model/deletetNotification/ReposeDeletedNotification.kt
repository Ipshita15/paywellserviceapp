package com.cloudwell.paywell.services.activity.notification.model.deletetNotification

import com.google.gson.annotations.SerializedName

class ReposeDeletedNotification {
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Int = 0

}