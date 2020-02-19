package com.cloudwell.paywell.services.activity.refill.banktransfer.model

import com.google.gson.annotations.SerializedName

data class CurrentDistrict(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)