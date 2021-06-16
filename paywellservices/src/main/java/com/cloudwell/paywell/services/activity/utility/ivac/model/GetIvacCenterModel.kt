package com.cloudwell.paywell.services.activity.utility.ivac.model

import com.google.gson.annotations.SerializedName

data class GetIvacCenterModel(
        @field:SerializedName("username")
    var username: String = ""
)