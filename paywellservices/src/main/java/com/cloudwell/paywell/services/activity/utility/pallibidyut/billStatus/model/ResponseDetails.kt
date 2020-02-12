package com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.model

import com.google.gson.annotations.SerializedName

class ResponseDetails {
    @SerializedName("CallCenter")
    var callCenter: String = ""
    @SerializedName("message")
    var message: String = ""
    @SerializedName("OutletAddress")
    var outletAddress: String = ""
    @SerializedName("OutletName")
    var outletName: String = ""
    @SerializedName("RetailerCode")
    var retailerCode: String = ""
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("TrxId")
    var trxId: String = ""
}