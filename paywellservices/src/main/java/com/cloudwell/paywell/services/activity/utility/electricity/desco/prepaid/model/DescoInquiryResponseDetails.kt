package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model

import com.cloudwell.paywell.services.utils.ParameterUtility
import com.google.gson.annotations.SerializedName

class DescoInquiryResponseDetails {

    @SerializedName("message")
    var message: String = ""
    @SerializedName("msg_text")
    var msgText: String = ""
    @SerializedName(ParameterUtility.KEY_REF_ID)
    var refId: String = ""
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("total_amount")
    var totalAmount: Int = 0
    @SerializedName("trans_id")
    var transId: String = ""

}