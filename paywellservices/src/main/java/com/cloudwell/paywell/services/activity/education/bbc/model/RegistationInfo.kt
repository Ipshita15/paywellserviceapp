package com.cloudwell.paywell.services.activity.education.bbc.model

import com.google.gson.annotations.SerializedName

data class RegistationInfo (

    @field: SerializedName("username")
    var username : String = "",

            @field: SerializedName("mobile")
            var mobile : String = ""
)