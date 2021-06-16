package com.cloudwell.paywell.services.activity.refill.model

import com.google.gson.annotations.SerializedName

class RequestDistrict {
    @SerializedName("username")
    var mUsername: String = ""
    @SerializedName("bankId")
    var mBankId: String = ""
    @SerializedName("ref_id")
    var ref_id: String = ""


}