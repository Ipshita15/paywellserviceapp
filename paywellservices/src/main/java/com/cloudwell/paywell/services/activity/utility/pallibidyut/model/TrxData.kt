package com.cloudwell.paywell.services.activity.utility.pallibidyut.model

data class TrxData(
    val AccountNo: String,
    val BillAmount: Double,
    val BillNo: String,
    val CustomerName: String,
    val CustomerPhone: String,
    val StatusCode: Int,
    val TrxId: String
)