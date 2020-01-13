package com.cloudwell.paywell.services.activity.utility.pallibidyut.model

import com.google.gson.annotations.SerializedName

data class REBNotification(

        @SerializedName("ServiceType")
        val ServiceType: String,

        @SerializedName("TrxData")
        val TrxData: TrxData
)