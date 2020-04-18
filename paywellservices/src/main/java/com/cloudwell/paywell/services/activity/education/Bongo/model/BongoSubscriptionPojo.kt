package com.cloudwell.paywell.services.activity.education.Bongo.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/31/2020.
 */
data class BongoSubscriptionPojo (

        @field:SerializedName("username")
        var username: String? = "",

        @field:SerializedName("pin_no")
        var pin_no: String? = ""
)