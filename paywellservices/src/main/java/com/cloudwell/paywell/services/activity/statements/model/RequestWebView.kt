package com.cloudwell.paywell.services.activity.statements.model

import com.google.gson.annotations.SerializedName

class RequestWebView {
    @SerializedName("language")
    var language: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("username")
    var username: String = ""

}