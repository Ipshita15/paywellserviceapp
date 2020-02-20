package com.cloudwell.paywell.services.activity.refill.banktransfer.model

import com.google.gson.annotations.SerializedName

data class BankInfo(
        @SerializedName("accountNumber")
        val accountNumber: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("accountName")
        val accountName: String,
        @SerializedName("branch")
        val branch: String

)