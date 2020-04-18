package com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/8/2020.
 */
data class Nagadv2requestPojo (

    @field:SerializedName("username")
    var username : String = "",

    @field:SerializedName("amount")
    var amount : String = "",

    @field:SerializedName("password")
    var password: String = ""
)