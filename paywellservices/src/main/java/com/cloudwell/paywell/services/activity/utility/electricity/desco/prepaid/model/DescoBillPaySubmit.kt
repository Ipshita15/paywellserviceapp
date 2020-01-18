package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model


import com.cloudwell.paywell.services.utils.ParameterUtility
import com.google.gson.annotations.SerializedName

class DescoBillPaySubmit {
    @SerializedName("billNo")
    var billNo: String = ""
    @SerializedName("format")
    var format: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("payerMobileNo")
    var payerMobileNo: String = ""
    @SerializedName("totalAmount")
    var totalAmount: String = ""
    @SerializedName("transId")
    var transId: String = ""
    @SerializedName("username")
    var username: String = ""
    @SerializedName(ParameterUtility.KEY_REF_ID)
    var refId: String = ""

}