package com.cloudwell.paywell.services.activity.refill.banktransfer.model

import com.google.gson.annotations.SerializedName

class Branch {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("name")
    var name: String? = null

}