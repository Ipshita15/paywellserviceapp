package com.cloudwell.paywell.services.activity.utility.ivac.model

import com.google.gson.annotations.SerializedName

data class IvacTrxListModel(

        @field:SerializedName("limit")
    var limit: String = "",
        @field:SerializedName("password")
    var password: String = "",
        @field:SerializedName("username")
    var username: String = ""
)