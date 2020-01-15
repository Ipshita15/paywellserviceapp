package com.cloudwell.paywell.services.activity.utility.pallibidyut.model

import com.google.gson.annotations.SerializedName

data class TrxData(

    @SerializedName("AccountNo")
    val AccountNo: String,
    @SerializedName("BillAmount")
    val BillAmount: Double,
    @SerializedName("BillNo")
    val BillNo: String,
    @SerializedName("CustomerName")
    val CustomerName: String,
    @SerializedName("CustomerPhone")
    val CustomerPhone: String,
    @SerializedName("StatusCode")
    val StatusCode: Int,
    @SerializedName("TrxId")
    val TrxId: String
)