package com.cloudwell.paywell.services.activity.entertainment.bongo.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/31/2020.
 */
data class BongoActivePkgPojo (

        @field:SerializedName("username")
        var username : String? = "",

        @field:SerializedName("pin_no")
        var pin_no : String? = "",

        @field:SerializedName("code")
        var code  : String? = "",

        @field:SerializedName("locale")
        var locale  : String? = "",

        @field:SerializedName("mobile")
        var mobile  : String? = ""
)