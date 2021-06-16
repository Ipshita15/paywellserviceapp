package com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.model

import com.google.gson.annotations.SerializedName

class RequestPBRegistioin {
    @SerializedName("account_no")
    var accoUntNo: String = ""
    @SerializedName("cust_name")
    var custName: String = ""
    @SerializedName("cust_phn")
    var custPHn: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("username")
    var username: String = ""

}