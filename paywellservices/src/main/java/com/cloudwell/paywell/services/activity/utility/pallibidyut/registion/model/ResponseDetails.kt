package com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.model

import com.google.gson.annotations.SerializedName

class ResponseDetails {
    @SerializedName("CallCenter")
    var callCenter: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("OutletAddress")
    var outletAddress: String? = null
    @SerializedName("OutletName")
    var outletName: String? = null
    @SerializedName("RetailerCode")
    var retailerCode: String? = null
    @SerializedName("status")
    var status: Long? = null
    @SerializedName("TrxId")
    var trxId: String? = null

}