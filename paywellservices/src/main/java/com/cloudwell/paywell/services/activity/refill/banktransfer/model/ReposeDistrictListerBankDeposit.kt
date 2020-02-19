package com.cloudwell.paywell.services.activity.refill.banktransfer.model

data class ReposeDistrictListerBankDeposit(
        val bankInfo: BankInfo,
        val branch: List<Branch>,
        val currentDistrict: CurrentDistrict,
        val districtData: List<DistrictData>,
        val message: String,
        val status: Int
)