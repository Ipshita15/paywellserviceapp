package com.cloudwell.paywell.services.activity.utility.pallibidyut.model

import com.google.gson.annotations.SerializedName

class ReqInquiryModel {
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("username")
    var username: String  = ""
    @SerializedName("service")
    var service: String  = ""
    @SerializedName("limit")
    var limit: String  = ""

}