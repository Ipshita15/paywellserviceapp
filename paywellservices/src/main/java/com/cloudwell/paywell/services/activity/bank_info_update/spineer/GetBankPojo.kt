package com.cloudwell.paywell.services.activity.bank_info_update.spineer

import com.google.gson.annotations.SerializedName

/**
 * Created by Sepon on 3/12/2020.
 */
data class GetBankPojo(
        @field: SerializedName ("username")
        var username : String? = "",

        @field: SerializedName ("ref_id")
        var ref_id : String = ""
)
