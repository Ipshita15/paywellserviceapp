package com.cloudwell.paywell.services.activity.refill.banktransfer.model

import com.google.gson.annotations.SerializedName

data class ReposeDistrictListerBankDeposit(
        @SerializedName("bankInfo")
        val bankInfo: BankInfo,
        @SerializedName("branch")
        val branch: List<Branch>,
        @SerializedName("currentDistrict")
        val currentDistrict: CurrentDistrict,

        @SerializedName("districtData")
        val districtData: List<DistrictData>,

        @SerializedName("message")
        val message: String,
        @SerializedName("status")
        val status: Int
)