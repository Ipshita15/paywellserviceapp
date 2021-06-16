package com.cloudwell.paywell.services.activity.utility.ivac.model

import com.google.gson.annotations.SerializedName

data class GetIvacTrx(
        @field:SerializedName("username")
    var username: String = "",
        @field:SerializedName("web_file_no")
    var web_file_no: String = ""
)