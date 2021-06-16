package com.cloudwell.paywell.services.activity.bank_info_update.spineer

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/10/2020.
 */
data class SpinnerBankPojo (
        @field: SerializedName("name")
        var name : String? = "",

        @field: SerializedName("id")
        var id : String? = ""
)