package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

data class RequestAppsAuth(
        @SerializedName("debug")
        val debug: Int,

        @SerializedName("deviceId")
        val deviceId: String,

        @SerializedName("magicCard")
        val magicCard: String ,

        @SerializedName("channel")
        val channel: String,

        @SerializedName("timestamp")
        val timestamp: String


)