package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog

import com.google.gson.annotations.SerializedName

class CustomerInfo {

    @SerializedName("customerAge")
    var customerAge: String = ""
    @SerializedName("customerEmail")
    var customerEmail: String = ""
    @SerializedName("customerName")
    var customerName: String = ""
    @SerializedName("customerPhone")
    var customerPhone: String = ""

    @SerializedName("customerGender")
    val cusTomerGenger: String = ""
    @SerializedName("customerAddress")
    var customerAddress: String = ""
}
