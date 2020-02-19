package com.cloudwell.paywell.services.activity.refill.banktransfer.model

import com.google.gson.annotations.SerializedName

data class DistrictData(
        @SerializedName("district_name")
        val district_name: String,
        @SerializedName("id")
        val id: String
)