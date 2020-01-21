package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model

import com.cloudwell.paywell.services.utils.ParameterUtility
import com.google.gson.annotations.SerializedName

class DescoRequestInquiryModel {
    @SerializedName("amount")
    var amount: String = ""
    @SerializedName("billNo")
    var billNo: String = ""
    @SerializedName("format")
    var format: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("payerMobileNo")
    var payerMobileNo: String = ""
    @SerializedName(ParameterUtility.KEY_REF_ID)
    var refId: String = ""
    @SerializedName("username")
    var username: String = ""

}