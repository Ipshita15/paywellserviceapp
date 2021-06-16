package com.cloudwell.paywell.services.activity.entertainment.bongo.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/4/2020.
 */
data class BongoTRXrequestModel(
        @field: SerializedName("username")
        var userName : String = "",
        @field: SerializedName("service")
        var service : String = "",
        @field: SerializedName("limit")
        var limit : String = ""


)