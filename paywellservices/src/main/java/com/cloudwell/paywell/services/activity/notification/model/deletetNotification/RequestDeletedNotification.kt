package com.cloudwell.paywell.services.activity.notification.model.deletetNotification

import com.google.gson.annotations.SerializedName

class RequestDeletedNotification {
    @SerializedName("message_id")
    var messageId: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("username")
    var username: String = ""

}