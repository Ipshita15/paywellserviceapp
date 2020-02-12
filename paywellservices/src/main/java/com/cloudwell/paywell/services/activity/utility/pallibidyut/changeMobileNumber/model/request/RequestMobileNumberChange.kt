package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.request

import com.google.gson.annotations.SerializedName

class RequestMobileNumberChange {
    @SerializedName("account_no")
    var accountNo: String = ""
    @SerializedName("cust_phn")
    var custPhn: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("username")
    var username: String = ""

}