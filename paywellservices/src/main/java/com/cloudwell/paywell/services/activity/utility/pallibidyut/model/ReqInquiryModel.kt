package com.cloudwell.paywell.services.activity.utility.pallibidyut.model

import com.google.gson.annotations.SerializedName

class ReqInquiryModel {
    @SerializedName("account_no")
    var accountNo: String = ""
    @SerializedName("month")
    var month: String  = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("username")
    var username: String  = ""
    @SerializedName("year")
    var year: String  = ""

}